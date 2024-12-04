package by.course.govservices.repositories

import by.course.govservices.entities.GovService
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface ServiceRepository : JpaRepository<GovService, Long>, JpaSpecificationExecutor<GovService> {

    // Динамическое выполнение запроса с условиями
    @Query("SELECT gs FROM GovService gs WHERE gs.name LIKE %:name%")
    fun findByName(@Param("name") name: String): List<GovService>
}
