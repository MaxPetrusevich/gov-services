import by.course.govservices.entities.Payment
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.time.LocalDate

interface PaymentRepository : ReactiveCrudRepository<Payment, Long> {
    // Поиск сервиса по его коду
    fun findByDate(date: LocalDate): Mono<Payment>
}
