package by.course.govservices.repositories

import by.course.govservices.entities.Bid
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface BidRepository : ReactiveCrudRepository<Bid, Long> {

    // Поиск заявок по идентификатору гражданина
    fun findByCitizenId(citizenId: Long): Flux<Bid>

    // Метод для поиска заявок с учетом фильтров и пагинации
    // Мы будем передавать строку SQL вместо Query
    @Query("SELECT * FROM bid WHERE :whereClause")
    fun findAllWithFilters(whereClause: String): Flux<Bid>
}
