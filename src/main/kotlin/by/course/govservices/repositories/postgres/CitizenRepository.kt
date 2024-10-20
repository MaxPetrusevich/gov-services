package by.course.govservices.repositories.postgres
import by.course.govservices.entities.Citizen
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
@Repository
interface CitizenRepository : ReactiveCrudRepository<Citizen, Long> {
    // Поиск гражданина по его имени
    fun findByFirstName(name: String): Mono<Citizen>
    fun findByIdentifyNumber(identifier: String): Mono<Citizen>
}
