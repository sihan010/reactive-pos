package dev.sihan.pos.model

import org.springframework.data.annotation.Id
import java.time.OffsetDateTime
import java.util.*

data class PaymentMethod(
    @Id
    val paymentMethodId: UUID?,
    val paymentMethod: String,
    val priceModifierFrom: Float,
    val priceModifierTo: Float,
    val points: Float,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
