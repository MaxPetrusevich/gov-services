import by.course.govservices.entities.PaymentStatus
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface PaymentStatusRepository : ReactiveCrudRepository<PaymentStatus, Long> {
    // Поиск сервиса по его коду
    fun findByStatus(code: String): Mono<PaymentStatus>
}
