package by.course.govservices.repositories

import by.course.govservices.entities.Establishment
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface EstablishmentRepository : ReactiveCrudRepository<Establishment, Int> {

    // Динамическое выполнение запроса с условиями
    @Query("SELECT * FROM establishment WHERE :whereClause")
    fun findByDynamicFilter(whereClause: String): Flux<Establishment>

    fun findByName(name: String): Mono<Establishment>

}
