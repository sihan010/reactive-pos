package dev.sihan.pos.controller

import dev.sihan.pos.dto.PaymentMethodIn
import dev.sihan.pos.model.PaymentMethod
import dev.sihan.pos.service.PaymentMethodService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 Mix Coroutine with WebFlux
 https://blog.allegro.tech/2020/02/webflux-and-coroutines.html
*/

@Controller
class PaymentMethodController(private val paymentMethodService: PaymentMethodService) {
    @QueryMapping("GetAllPaymentMethods")
    fun getAllPaymentMethods(): Flux<PaymentMethod> {
        return paymentMethodService.getAllPaymentMethods()
    }

    @QueryMapping("GetSinglePaymentMethodByType")
    fun getSinglePaymentMethodByType(@Argument type: String): Mono<PaymentMethod> {
        return paymentMethodService.getSinglePaymentMethodByType(type)
    }

    @MutationMapping("AddSinglePaymentMethod")
    fun addPaymentMethod(@Argument paymentMethod: PaymentMethodIn): Mono<PaymentMethod> {
        return paymentMethodService.addPaymentMethod(paymentMethod)
    }
}
