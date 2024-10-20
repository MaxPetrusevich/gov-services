package by.course.govservices.repositories.postgres
import by.course.govservices.entities.Payment
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDate
@Repository
interface PaymentRepository : ReactiveCrudRepository<Payment, Long> {
    fun findByDate(date: LocalDate): Mono<Payment>
}
