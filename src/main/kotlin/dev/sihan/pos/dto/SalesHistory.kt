package dev.sihan.pos.dto

import java.time.OffsetDateTime

data class SalesHistoryOut(
    val dateTime: OffsetDateTime,
    val sales: String,
    val points: Float
)
