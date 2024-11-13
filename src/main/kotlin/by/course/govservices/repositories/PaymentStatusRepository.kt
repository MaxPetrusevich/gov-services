package by.course.govservices.repositories

import by.course.govservices.entities.PaymentStatus
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@Repository
interface PaymentStatusRepository : ReactiveCrudRepository<PaymentStatus, Long> {

    // Поиск по статусу
    fun findByStatus(code: String): Mono<PaymentStatus>

    // Можно добавить метод для получения всех статусов с фильтрацией, если необходимо
    fun findAllByStatusContainingIgnoreCase(status: String): Flux<PaymentStatus>
}
