package dev.sihan.pos.service

import dev.sihan.pos.common.PosApplicationError
import dev.sihan.pos.dto.PaymentMethodIn
import dev.sihan.pos.dto.toModel
import dev.sihan.pos.dto.validate
import dev.sihan.pos.model.PaymentMethod
import dev.sihan.pos.repository.PaymentMethodRepository
import graphql.ErrorType
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PaymentMethodService(private val paymentMethodRepository: PaymentMethodRepository) {
    fun getAllPaymentMethods(): Flux<PaymentMethod> = paymentMethodRepository.findAll()

    suspend fun getSinglePaymentMethodByType(type: String) =
        paymentMethodRepository.findByPaymentMethod(type).awaitSingleOrNull().let {
            if (it == null) {
                throw PosApplicationError(
                    ErrorType.DataFetchingException,
                    "Payment method with type $type was not found"
                )
            }
            it
        }

    suspend fun addPaymentMethod(paymentMethodIn: PaymentMethodIn): Mono<PaymentMethod> {
        paymentMethodIn.validate()
        return paymentMethodRepository.findByPaymentMethod(paymentMethodIn.paymentMethod).awaitSingleOrNull().let {
            if (it != null) {
                throw PosApplicationError(
                    ErrorType.ValidationError,
                    "Payment method with type ${paymentMethodIn.paymentMethod} already exists"
                )
            }
            paymentMethodRepository.save(paymentMethodIn.toModel())
        }
    }
}
