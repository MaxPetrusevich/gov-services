package by.course.govservices.repositories

import by.course.govservices.entities.Payment
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
interface PaymentRepository : ReactiveCrudRepository<Payment, Long> {

    // Динамическое выполнение запроса с условиями
    @Query("SELECT * FROM payment WHERE :whereClause")
    fun findByDynamicFilter(whereClause: String): Flux<Payment>

    // Пример поиска платежа по дате
    fun findByDate(date: LocalDate): Mono<Payment>
}
