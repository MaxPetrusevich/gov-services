package by.course.govservices.repositories.postgres
import by.course.govservices.entities.Establishment
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
@Repository
interface EstablishmentRepository : ReactiveCrudRepository<Establishment, Long> {
    fun findByName(code: String): Mono<Establishment>
}
