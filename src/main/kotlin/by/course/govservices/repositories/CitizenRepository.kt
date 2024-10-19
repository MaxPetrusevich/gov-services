import by.course.govservices.entities.Citizen
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface CitizenRepository : ReactiveCrudRepository<Citizen, Long> {
    // Поиск гражданина по его имени
    fun findByFirstName(name: String): Mono<Citizen>
}
