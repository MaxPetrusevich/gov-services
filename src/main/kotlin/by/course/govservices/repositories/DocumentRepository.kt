package by.course.govservices.repositories

import by.course.govservices.entities.Document
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface DocumentRepository : ReactiveCrudRepository<Document, Int> {

    // Динамическое выполнение запроса с условиями
    @Query("SELECT * FROM document WHERE :whereClause")
    fun findByDynamicFilter(whereClause: String): Flux<Document>

    fun findByLink(link: String): Mono<Document>
    fun findByTypeId(typeId: Int): Flux<Document>
}
