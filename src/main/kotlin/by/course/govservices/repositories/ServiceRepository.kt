import by.course.govservices.entities.Service
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface ServiceRepository : ReactiveCrudRepository<Service, Long> {
    // Поиск сервиса по его коду
    fun findByName(code: String): Mono<Service>
}
