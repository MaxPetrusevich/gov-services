import by.course.govservices.entities.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface UserRepository : ReactiveCrudRepository<User, Long> {
    // Поиск сервиса по его коду
    fun findById(id: Int): Mono<User>
}
