package dev.sihan.pos.service

import dev.sihan.pos.common.PosApplicationError
import dev.sihan.pos.dto.PaymentIn
import dev.sihan.pos.dto.PaymentOut
import dev.sihan.pos.dto.SalesHistoryOut
import dev.sihan.pos.dto.validate
import dev.sihan.pos.model.Sales
import dev.sihan.pos.model.toSalesHistoryOut
import dev.sihan.pos.repository.PaymentMethodRepository
import dev.sihan.pos.repository.SalesRepository
import graphql.ErrorType
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

@Service
class SalesService(
    private val paymentMethodRepository: PaymentMethodRepository,
    private val salesRepository: SalesRepository
) {
    fun addSale(paymentIn: PaymentIn): Mono<PaymentOut> {
        // Input validation
        try {
            paymentIn.validate()
        } catch (ex: PosApplicationError) {
            return Mono.error(ex)
        }

        return paymentMethodRepository.findByPaymentMethod(paymentIn.paymentMethod).doOnError {
            throw PosApplicationError(
                ErrorType.ExecutionAborted,
                "Internal server error"
            )
        }.switchIfEmpty(
            Mono.error(
                PosApplicationError(
                    ErrorType.ValidationError,
                    "Payment method with type ${paymentIn.paymentMethod} was not found"
                )
            )
        ).flatMap { paymentMethod ->
            // Validation
            if (paymentIn.priceModifier < paymentMethod.priceModifierFrom ||
                paymentIn.priceModifier > paymentMethod.priceModifierTo
            ) {
                throw PosApplicationError(
                    ErrorType.ValidationError,
                    """Price Modifier must be between ${paymentMethod.priceModifierFrom} 
                        |and ${paymentMethod.priceModifierTo}
                    """.trimMargin()
                )
            }
            // Business logic
            val finalPrice = paymentIn.price.toFloat() * paymentIn.priceModifier
            val points = paymentIn.price.toFloat() * paymentMethod.points
            // Persistence
            salesRepository.save(
                Sales(
                    null,
                    finalPrice,
                    points,
                    OffsetDateTime.parse(paymentIn.dateTime),
                    OffsetDateTime.now()
                )
            ).doOnError {
                throw PosApplicationError(
                    ErrorType.ExecutionAborted,
                    "Internal server error"
                )
            }.flatMap {
                if (it == null) {
                    throw PosApplicationError(
                        ErrorType.ExecutionAborted,
                        "Could not make payment"
                    )
                }
                Mono.just(PaymentOut(it.sales.toString(), it.points))
            }
        }
    }

    fun getSalesHistory(startDateTime: String, endDateTime: String): Flux<SalesHistoryOut> {
        return try {
            val validatedStart = OffsetDateTime.parse(startDateTime)
            val validatedEnd = OffsetDateTime.parse(endDateTime)
            salesRepository.findByCreatedAtBetween(validatedStart, validatedEnd).map {
                it.toSalesHistoryOut()
            }
        } catch (parseError: DateTimeParseException) {
            Flux.error(
                PosApplicationError(
                    ErrorType.ExecutionAborted,
                    "Invalid date range"
                )
            )
        }
    }
}
