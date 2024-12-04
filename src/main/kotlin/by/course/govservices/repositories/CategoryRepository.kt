package by.course.govservices.repositories

import by.course.govservices.entities.BidStatus
import by.course.govservices.entities.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface CategoryRepository : JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    // Кастомный метод для поиска категорий с фильтрацией
    @Query("SELECT c FROM Category c WHERE c.category LIKE %:name%")
    fun findByNameContaining(@Param("name") name: String): List<Category>
}
