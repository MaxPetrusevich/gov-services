package by.course.govservices.service

import by.course.govservices.dto.DocumentDto
import by.course.govservices.entities.Document
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.DocumentRepository
import by.course.govservices.service.base.BaseService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class DocumentService(
    private val documentRepository: DocumentRepository
) : BaseService<DocumentDto, Int> {  // Используем Int для идентификаторов документов

    override fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Flux<DocumentDto> {
        // Строим динамическую строку для фильтров
        val whereClause = buildDynamicFilterClause(filters)

        return documentRepository.findByDynamicFilter(whereClause)
            .skip((pagination.page * pagination.size).toLong()) // Пропускаем записи на основе пагинации
            .take(pagination.size.toLong()) // Берем нужное количество записей
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Documents not found")))
    }

    override fun findById(id: Int): Mono<DocumentDto> {  // Используем Int для идентификаторов
        return documentRepository.findById(id)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Document with ID $id not found")))
    }

    override fun save(entity: DocumentDto): Mono<DocumentDto> {
        return documentRepository.save(entity.toEntity())
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to save document: ${e.message}") }
    }

    override fun update(id: Int, entity: DocumentDto): Mono<DocumentDto> {  // Используем Int для идентификаторов
        return documentRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Document with ID $id not found")))
            .flatMap { documentRepository.save(entity.toEntity()) }
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to update document: ${e.message}") }
    }

    override fun delete(id: Int): Mono<Void> {  // Используем Int для идентификаторов
        return documentRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Document with ID $id not found")))
            .flatMap { documentRepository.delete(it) }
            .onErrorMap { e -> DataFormatException("Failed to delete document: ${e.message}") }
    }

    // Метод для построения динамической строки фильтров для SQL-запроса
    private fun buildDynamicFilterClause(filters: List<FilterCriteria>): String {
        val filterClauses = mutableListOf<String>()

        filters.forEach { filter ->
            when (filter.field) {
                "link" -> filterClauses.add("link LIKE '%${filter.value}%'")
                "typeId" -> filterClauses.add("type_id = ${filter.value.toInt()}")
                "loadingDate" -> filterClauses.add("loading_date = '${filter.value}'")
                "bidId" -> filterClauses.add("bid_id = ${filter.value.toInt()}")
                else -> {} // Пропускаем неизвестные фильтры
            }
        }

        return filterClauses.joinToString(" AND ")
    }
}

// Преобразование Document в DTO и наоборот
fun Document.toDto(): DocumentDto = DocumentDto(id, link, typeId, loadingDate, bidId)
fun DocumentDto.toEntity(): Document = Document(id, link, typeId, loadingDate, bidId)
