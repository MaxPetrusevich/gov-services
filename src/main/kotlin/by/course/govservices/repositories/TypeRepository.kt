import by.course.govservices.entities.Type
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface TypeRepository : ReactiveCrudRepository<Type, Long> {
    // Поиск сервиса по его коду
    fun findByType(code: String): Mono<Type>
}
