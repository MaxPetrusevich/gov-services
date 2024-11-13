package by.course.govservices.repositories

import by.course.govservices.entities.BidStatus
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface BidStatusRepository : ReactiveCrudRepository<BidStatus, Int> {

    // Динамическое выполнение запроса с условиями
    @Query("SELECT * FROM bid_status WHERE :whereClause")
    fun findByDynamicFilter(whereClause: String): Flux<BidStatus>

    fun findByStatus(status: String): Mono<BidStatus>
}
