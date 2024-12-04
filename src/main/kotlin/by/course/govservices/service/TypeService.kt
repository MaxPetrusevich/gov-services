package by.course.govservices.service

import by.course.govservices.dto.TypeDto
import by.course.govservices.entities.Type
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.TypeRepository
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class TypeService(
    private val typeRepository: TypeRepository
) {

    /**
     * Получение всех типов с фильтрацией и пагинацией.
     */
    fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Page<TypeDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)
        val specification = buildSpecification(filters)

        val types = typeRepository.findAll(specification, pageRequest)
        if (types.isEmpty) {
            throw NotFoundException("Types not found")
        }

        return types.map { it.toDto() }
    }

    /**
     * Получение типа по ID.
     */
    fun findById(id: Long): TypeDto {
        val type = typeRepository.findById(id)
            .orElseThrow { NotFoundException("Type with ID $id not found") }
        return type.toDto()
    }

    /**
     * Создание нового типа.
     */
    fun save(dto: TypeDto): TypeDto {
        return try {
            typeRepository.save(dto.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to save type: ${e.message}")
        }
    }

    /**
     * Обновление существующего типа.
     */
    fun update(id: Long, dto: TypeDto): TypeDto {
        val existingType = typeRepository.findById(id)
            .orElseThrow { NotFoundException("Type with ID $id not found") }

        return try {
            typeRepository.save(dto.toEntity(existingType)).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to update type: ${e.message}")
        }
    }

    /**
     * Удаление типа по ID.
     */
    fun delete(id: Long) {
        val type = typeRepository.findById(id)
            .orElseThrow { NotFoundException("Type with ID $id not found") }

        try {
            typeRepository.delete(type)
        } catch (e: Exception) {
            throw DataFormatException("Failed to delete type: ${e.message}")
        }
    }

    /**
     * Создание спецификации для фильтров.
     */
    private fun buildSpecification(filters: List<FilterCriteria>): Specification<Type> {
        return Specification { root, _, cb ->
            filters.mapNotNull { filter ->
                when (filter.field) {
                    "type" -> cb.like(cb.lower(root.get("type")), "%${filter.value.lowercase()}%")
                    else -> null
                }
            }.reduceOrNull { a, b -> cb.and(a, b) }
        }
    }
}

// Преобразование Type в DTO
fun Type.toDto(): TypeDto = TypeDto(
    id = this.id,
    type = this.type ?: throw IllegalStateException("Type must not be null")
)

// Преобразование TypeDto в сущность
fun TypeDto.toEntity(existingEntity: Type? = null): Type = Type(
    id = existingEntity?.id ?: this.id,
    type = this.type
)
