package dev.sihan.pos.repository

import dev.sihan.pos.model.Sales
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import java.time.OffsetDateTime
import java.util.UUID

interface SalesRepository : R2dbcRepository<Sales, UUID> {
    fun findByCreatedAtBetween(from: OffsetDateTime, to: OffsetDateTime): Flux<Sales>

    @Query("SELECT * FROM sales WHERE created_at BETWEEN :#{[0]} AND :#{[1]}")
    fun findByCreatedAtBetweenCustom(from: OffsetDateTime, to: OffsetDateTime): Flux<Sales>
}
