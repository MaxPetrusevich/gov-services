package by.course.govservices.repositories.postgres
import by.course.govservices.entities.Role
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
@Repository
interface RoleRepository : ReactiveCrudRepository<Role, Long> {
    fun findByRole(code: String): Mono<Role>
}
