package dev.sihan.pos.controller

import dev.sihan.pos.dto.PaymentIn
import dev.sihan.pos.dto.PaymentOut
import dev.sihan.pos.dto.SalesHistoryOut
import dev.sihan.pos.service.SalesService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class SalesController(private val salesService: SalesService) {

    @MutationMapping("MakePayment")
    fun makePayment(@Argument payment: PaymentIn): Mono<PaymentOut> {
        return salesService.addSale(payment)
    }

    @QueryMapping("GetSalesHistory")
    fun getSalesHistory(@Argument startDateTime: String, @Argument endDateTime: String): Flux<SalesHistoryOut> {
        return salesService.getSalesHistory(startDateTime, endDateTime)
    }
}
