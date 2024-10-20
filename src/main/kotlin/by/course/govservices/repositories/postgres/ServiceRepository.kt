package by.course.govservices.repositories.postgres
import by.course.govservices.entities.Service
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
@Repository
interface ServiceRepository : ReactiveCrudRepository<Service, Long> {
    fun findByName(code: String): Mono<Service>
}
