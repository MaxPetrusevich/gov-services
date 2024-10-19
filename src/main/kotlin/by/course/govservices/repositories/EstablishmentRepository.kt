import by.course.govservices.entities.Establishment
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface EstablishmentRepository : ReactiveCrudRepository<Establishment, Long> {
    // Поиск сервиса по его коду
    fun findByName(code: String): Mono<Establishment>
}
