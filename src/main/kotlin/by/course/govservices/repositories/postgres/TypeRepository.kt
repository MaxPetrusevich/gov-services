package by.course.govservices.repositories.postgres

import by.course.govservices.entities.Type
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
@Repository
interface TypeRepository : ReactiveCrudRepository<Type, Long> {
    fun findByType(code: String): Mono<Type>
}
