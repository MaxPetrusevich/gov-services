package by.course.govservices.repositories

import by.course.govservices.entities.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface DocumentRepository : JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {

    // Динамическое выполнение запроса с условиями
    @Query("SELECT d FROM Document d WHERE d.link LIKE %:link%")
    fun findByLink(@Param("link") link: String): Document?

    fun findByTypeId(typeId: Int): List<Document>
}
