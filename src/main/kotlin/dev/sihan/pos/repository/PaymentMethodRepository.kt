package dev.sihan.pos.repository

import dev.sihan.pos.model.PaymentMethod
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono
import java.util.UUID

interface PaymentMethodRepository : R2dbcRepository<PaymentMethod, UUID> {
    fun findByPaymentMethod(paymentMethod: String): Mono<PaymentMethod>
}
