package by.course.govservices.repositories.postgres
import by.course.govservices.entities.Category
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
@Repository
interface CategoryRepository : ReactiveCrudRepository<Category, Long> {
    fun findByCategory(code: String): Mono<Category>
}
