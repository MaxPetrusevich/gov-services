package by.course.govservices.repositories

import by.course.govservices.entities.Establishment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface EstablishmentRepository : JpaRepository<Establishment, Long>, JpaSpecificationExecutor<Establishment> {

    // Динамическое выполнение запроса с условиями
    @Query("SELECT e FROM Establishment e WHERE e.name LIKE %:name%")
    fun findByNameContaining(@Param("name") name: String): List<Establishment>
}
