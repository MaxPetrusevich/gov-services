import by.course.govservices.entities.Role
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface RoleRepository : ReactiveCrudRepository<Role, Long> {
    // Поиск сервиса по его коду
    fun findByRole(code: String): Mono<Role>
}
