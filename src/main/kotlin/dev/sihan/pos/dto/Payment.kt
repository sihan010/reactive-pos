package dev.sihan.pos.dto

import dev.sihan.pos.common.PosApplicationError
import graphql.ErrorType
import java.lang.NumberFormatException
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

data class PaymentIn(
    val price: String,
    val priceModifier: Float,
    val paymentMethod: String,
    val dateTime: String
)

data class PaymentOut(
    val finalPrice: String,
    val points: Float
)

fun PaymentIn.validate() {
    try {
        price.toFloat()
    } catch (ex: NumberFormatException) {
        throw PosApplicationError(ErrorType.ValidationError, "Price must be a number")
    }
    try {
        OffsetDateTime.parse(dateTime)
    } catch (ex: DateTimeParseException) {
        throw PosApplicationError(ErrorType.ValidationError, "Invalid timestamp")
    }
}
