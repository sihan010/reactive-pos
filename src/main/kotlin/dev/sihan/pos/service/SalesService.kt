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
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

@Service
class SalesService(
    private val paymentMethodRepository: PaymentMethodRepository,
    private val salesRepository: SalesRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun addSale(paymentIn: PaymentIn): PaymentOut {
        // Input validation
        paymentIn.validate()
        val paymentMethod = paymentMethodRepository.findByPaymentMethod(paymentIn.paymentMethod).awaitSingleOrNull()
            ?: throw PosApplicationError(
                ErrorType.DataFetchingException,
                "Payment method with type ${paymentIn.paymentMethod} was not found"
            )
        if (paymentIn.priceModifier < paymentMethod.priceModifierFrom ||
            paymentIn.priceModifier > paymentMethod.priceModifierTo
        ) {
            throw PosApplicationError(
                ErrorType.DataFetchingException,
                "Price Modifier must be between ${paymentMethod.priceModifierFrom} and ${paymentMethod.priceModifierTo}"
            )
        }
        // Business logic
        val finalPrice = paymentIn.price.toFloat() * paymentIn.priceModifier
        val points = paymentIn.price.toFloat() * paymentMethod.points
        // Persistence
        return salesRepository.save(
            Sales(
                null,
                finalPrice,
                points,
                OffsetDateTime.parse(paymentIn.dateTime),
                OffsetDateTime.now()
            )
        ).awaitSingleOrNull().let {
            if (it == null) {
                throw PosApplicationError(
                    ErrorType.ExecutionAborted,
                    "Could not make payment"
                )
            } else {
                PaymentOut(it.sales.toString(), it.points)
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
            logger.error("Invalid date range", parseError)
            throw PosApplicationError(
                ErrorType.ExecutionAborted,
                "Invalid date range"
            )
        }

        // return Flux.just(SalesHistoryOut(OffsetDateTime.now().toString(), "100.88", 10.1f))
    }
}
