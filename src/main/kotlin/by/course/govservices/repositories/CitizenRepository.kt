package by.course.govservices.repositories

import by.course.govservices.entities.Citizen
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface CitizenRepository : ReactiveCrudRepository<Citizen, Int> {

    // Динамическое выполнение запроса с условиями
    @Query("SELECT * FROM citizen WHERE :whereClause")
    fun findByDynamicFilter(whereClause: String): Flux<Citizen>

    fun findCitizenByIdentifyNumber(identity: String): Mono<Citizen>
    fun findByFirstName(name: String): Mono<Citizen>
}
