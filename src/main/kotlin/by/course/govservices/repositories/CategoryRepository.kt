import by.course.govservices.entities.Category
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface CategoryRepository : ReactiveCrudRepository<Category, Long> {
    // Поиск сервиса по его коду
    fun findByCategory(code: String): Mono<Category>
}
