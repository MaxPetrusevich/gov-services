package by.course.govservices.repositories.postgres
import by.course.govservices.entities.PaymentStatus
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
@Repository
interface PaymentStatusRepository : ReactiveCrudRepository<PaymentStatus, Long> {
    fun findByStatus(code: String): Mono<PaymentStatus>
}
