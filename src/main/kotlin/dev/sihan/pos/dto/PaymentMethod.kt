package dev.sihan.pos.dto

import dev.sihan.pos.common.PosApplicationError
import dev.sihan.pos.model.PaymentMethod
import graphql.ErrorType
import java.time.OffsetDateTime

data class PaymentMethodIn(
    val paymentMethod: String,
    val priceModifierFrom: Float,
    val priceModifierTo: Float,
    val points: Float,
)

fun PaymentMethodIn.toModel(): PaymentMethod = PaymentMethod(
    null,
    paymentMethod,
    priceModifierFrom,
    priceModifierTo,
    points,
    OffsetDateTime.now(),
    OffsetDateTime.now()
)

@Suppress("MagicNumber")
fun PaymentMethodIn.validate() {
    if (paymentMethod.isEmpty()) {
        throw PosApplicationError(ErrorType.ValidationError, "Payment method can't be empty")
    } else if (priceModifierFrom < 0.8 || priceModifierFrom > 1) {
        throw PosApplicationError(ErrorType.ValidationError, "PriceModifierFrom must be between 0.8 to 1.0")
    } else if (priceModifierTo < 0.95 || priceModifierFrom > 1.25) {
        throw PosApplicationError(ErrorType.ValidationError, "PriceModifierFrom must be between 0.95 to 1.25")
    }
}
