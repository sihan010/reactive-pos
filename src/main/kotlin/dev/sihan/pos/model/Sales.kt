package dev.sihan.pos.model

import dev.sihan.pos.dto.SalesHistoryOut
import org.springframework.data.annotation.Id
import java.time.OffsetDateTime
import java.util.UUID

data class Sales(
    @Id val salesId: UUID?,
    val sales: Float,
    val points: Float,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

fun Sales.toSalesHistoryOut() = SalesHistoryOut(
    createdAt.toString(),
    sales.toString(),
    points
)
