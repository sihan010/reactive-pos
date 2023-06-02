package dev.sihan.pos.service

import dev.sihan.pos.common.PosApplicationError
import dev.sihan.pos.dto.PaymentMethodIn
import dev.sihan.pos.dto.toModel
import dev.sihan.pos.dto.validate
import dev.sihan.pos.model.PaymentMethod
import dev.sihan.pos.repository.PaymentMethodRepository
import graphql.ErrorType
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PaymentMethodService(private val paymentMethodRepository: PaymentMethodRepository) {
    fun getAllPaymentMethods(): Flux<PaymentMethod> = paymentMethodRepository.findAll()

    fun getSinglePaymentMethodByType(type: String) =
        paymentMethodRepository.findByPaymentMethod(type).doOnError {
            throw PosApplicationError(
                ErrorType.ExecutionAborted,
                "Internal server error"
            )
        }.switchIfEmpty(
            Mono.error {
                PosApplicationError(
                    ErrorType.DataFetchingException,
                    "Payment method with type $type was not found"
                )
            }
        )

    fun addPaymentMethod(paymentMethodIn: PaymentMethodIn): Mono<PaymentMethod> {
        try {
            paymentMethodIn.validate()
        } catch (ex: PosApplicationError) {
            return Mono.error(ex)
        }
        return paymentMethodRepository.findByPaymentMethod(paymentMethodIn.paymentMethod).doOnError {
            throw PosApplicationError(
                ErrorType.ExecutionAborted,
                "Internal server error"
            )
        }.hasElement().flatMap {
            if (it) {
                throw PosApplicationError(
                    ErrorType.ValidationError,
                    "Payment method with type ${paymentMethodIn.paymentMethod} already exists"
                )
            }
            paymentMethodRepository.save(paymentMethodIn.toModel())
        }
    }
}
