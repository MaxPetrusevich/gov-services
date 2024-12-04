package by.course.govservices.service

import by.course.govservices.dto.DocumentDto
import by.course.govservices.entities.Bid
import by.course.govservices.entities.Document
import by.course.govservices.entities.Type
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.BidRepository
import by.course.govservices.repositories.DocumentRepository
import by.course.govservices.repositories.TypeRepository
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DocumentService(
    private val documentRepository: DocumentRepository,
    private val typeRepository: TypeRepository,  // Нужно для поиска Type
    private val bidRepository: BidRepository   // Нужно для поиска Bid
) {

    /**
     * Возвращает документы с учетом фильтров и пагинации.
     */
    fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Page<DocumentDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)
        val specification = buildSpecification(filters)

        val page = documentRepository.findAll(specification, pageRequest)
        if (page.isEmpty) {
            throw NotFoundException("Documents not found")
        }

        return page.map { it.toDto() }
    }

    /**
     * Находит документ по идентификатору.
     */
    fun findById(id: Long): DocumentDto {
        return documentRepository.findById(id)
            .map { it.toDto() }
            .orElseThrow { NotFoundException("Document with ID $id not found") }
    }

    /**
     * Сохраняет новый документ.
     */
    fun save(entity: DocumentDto): DocumentDto {
        val type = typeRepository.findById(entity.typeId)
            .orElseThrow { NotFoundException("Type with ID ${entity.typeId} not found") }

        val bid = entity.bidId?.let {
            bidRepository.findById(it).orElseThrow { NotFoundException("Bid with ID $it not found") }
        }

        return try {
            documentRepository.save(entity.toEntity(type, bid)).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to save document: ${e.message}")
        }
    }

    /**
     * Обновляет существующий документ.
     */
    fun update(id: Long, entity: DocumentDto): DocumentDto {
        documentRepository.findById(id)
            .orElseThrow { NotFoundException("Document with ID $id not found") }

        val type = typeRepository.findById(entity.typeId)
            .orElseThrow { NotFoundException("Type with ID ${entity.typeId} not found") }

        val bid = entity.bidId?.let {
            bidRepository.findById(it).orElseThrow { NotFoundException("Bid with ID $it not found") }
        }

        return try {
            documentRepository.save(entity.toEntity(type, bid)).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to update document: ${e.message}")
        }
    }

    /**
     * Удаляет документ по идентификатору.
     */
    fun delete(id: Long) {
        val existingDocument = documentRepository.findById(id)
            .orElseThrow { NotFoundException("Document with ID $id not found") }

        try {
            documentRepository.delete(existingDocument)
        } catch (e: Exception) {
            throw DataFormatException("Failed to delete document: ${e.message}")
        }
    }

    /**
     * Создает спецификацию для фильтров.
     */
    private fun buildSpecification(filters: List<FilterCriteria>): Specification<Document> {
        return Specification { root, _, cb ->
            filters.mapNotNull { filter ->
                when (filter.field) {
                    "link" -> cb.like(root.get<String>("link"), "%${filter.value}%")
                    "typeId" -> cb.equal(root.get<Type>("type").get<Int>("id"), filter.value.toInt())
                    "loadingDate" -> cb.equal(root.get<LocalDate>("loadingDate"), LocalDate.parse(filter.value))
                    "bidId" -> cb.equal(root.get<Bid>("bid").get<Int>("id"), filter.value.toInt())
                    else -> null
                }
            }.reduce { a, b -> cb.and(a, b) }
        }
    }

}

// Преобразование Document в DTO и наоборот
fun Document.toDto(): DocumentDto = DocumentDto(
    id = this.id,
    link = this.link ?: "",
    typeId = this.type?.id ?: throw IllegalStateException("Type must not be null"),
    loadingDate = this.loadingDate,
    bidId = this.bid?.id
)

fun DocumentDto.toEntity(type: Type, bid: Bid?): Document = Document(
    id = this.id,
    link = this.link,
    type = type, // Передаём сущность Type
    loadingDate = this.loadingDate,
    bid = bid // Передаём сущность Bid, если она присутствует
)
