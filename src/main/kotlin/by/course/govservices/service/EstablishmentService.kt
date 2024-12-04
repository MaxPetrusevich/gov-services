package by.course.govservices.service

import by.course.govservices.dto.EstablishmentDto
import by.course.govservices.entities.Establishment
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.EstablishmentRepository
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Page
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class EstablishmentService(
    private val establishmentRepository: EstablishmentRepository
) {

    /**
     * Возвращает все учреждения с учетом фильтров и пагинации.
     */
    fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Page<EstablishmentDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)
        val specification = buildSpecification(filters)

        val page = establishmentRepository.findAll(specification, pageRequest)
        if (page.isEmpty) {
            throw NotFoundException("Establishments not found")
        }

        return page.map { it.toDto() }
    }

    /**
     * Находит учреждение по идентификатору.
     */
    fun findById(id: Long): EstablishmentDto {
        return establishmentRepository.findById(id)
            .map { it.toDto() }
            .orElseThrow { NotFoundException("Establishment with ID $id not found") }
    }

    /**
     * Сохраняет новое учреждение.
     */
    fun save(entity: EstablishmentDto): EstablishmentDto {
        return try {
            establishmentRepository.save(entity.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to save establishment: ${e.message}")
        }
    }

    /**
     * Обновляет существующее учреждение.
     */
    fun update(id: Long, entity: EstablishmentDto): EstablishmentDto {
        establishmentRepository.findById(id)
            .orElseThrow { NotFoundException("Establishment with ID $id not found") }

        return try {
            establishmentRepository.save(entity.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to update establishment: ${e.message}")
        }
    }

    /**
     * Удаляет учреждение по идентификатору.
     */
    fun delete(id: Long) {
        val existingEstablishment = establishmentRepository.findById(id)
            .orElseThrow { NotFoundException("Establishment with ID $id not found") }

        try {
            establishmentRepository.delete(existingEstablishment)
        } catch (e: Exception) {
            throw DataFormatException("Failed to delete establishment: ${e.message}")
        }
    }

    /**
     * Создает спецификацию для фильтров.
     */
    private fun buildSpecification(filters: List<FilterCriteria>): Specification<Establishment> {
        return Specification { root, _, cb ->
            filters.mapNotNull { filter ->
                when (filter.field) {
                    "name" -> cb.like(root.get<String>("name"), "%${filter.value}%")
                    "address" -> cb.like(root.get<String>("address"), "%${filter.value}%")
                    "contact" -> cb.like(root.get<String>("contact"), "%${filter.value}%")
                    else -> null
                }
            }.reduce { a, b -> cb.and(a, b) }
        }
    }
}
fun Establishment.toDto(): EstablishmentDto = EstablishmentDto(
    id = this.id,
    name = this.name ?: ""
)

fun EstablishmentDto.toEntity(): Establishment = Establishment(
    id = this.id,
    name = this.name
)
