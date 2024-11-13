package by.course.govservices.repositories

import by.course.govservices.entities.GovService
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface ServiceRepository : ReactiveCrudRepository<GovService, Int> {

    // Динамическое выполнение запроса с условиями
    @Query("SELECT * FROM service WHERE :whereClause")
    fun findByDynamicFilter(whereClause: String): Flux<GovService>

    fun findByName(name: String): Mono<GovService>
}
