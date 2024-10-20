package by.course.govservices.repositories.postgres

import by.course.govservices.entities.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
@Repository
interface UserRepository : ReactiveCrudRepository<User, Int> {
    fun findByIdentifyNumber(identifyNumber: String): Mono<User>
}
