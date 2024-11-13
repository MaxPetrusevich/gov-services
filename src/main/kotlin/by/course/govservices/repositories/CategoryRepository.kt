package by.course.govservices.repositories

import by.course.govservices.entities.Category
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface CategoryRepository : ReactiveCrudRepository<Category, Int> {

    // Кастомный метод для поиска категорий с фильтрацией и пагинацией
    @Query("SELECT * FROM category WHERE :whereClause")
    fun findAllWithFilters(whereClause: String): Flux<Category>
}
