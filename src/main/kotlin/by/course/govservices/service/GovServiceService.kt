package by.course.govservices.service

import by.course.govservices.dto.ServiceDto
import by.course.govservices.entities.Category
import by.course.govservices.entities.Establishment
import by.course.govservices.entities.GovService
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.ServiceRepository
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class GovServiceService(
    private val serviceRepository: ServiceRepository
) {
    fun findById(
        id:Long
    ): ServiceDto{
        return serviceRepository.findById(id).orElseThrow().toDto()
    }
    fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Page<ServiceDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)
        val specification = buildSpecification(filters)

        val page = serviceRepository.findAll(specification, pageRequest)
        if (page.isEmpty) {
            throw NotFoundException("GovServices not found")
        }

        return page.map { it.toDto() }
    }

    fun findByName(name: String): List<ServiceDto> {
        val service = serviceRepository.findByName(name)
        return service.map{it.toDto()}
    }

    fun save(serviceDto: ServiceDto): ServiceDto {
        return try {
            serviceRepository.save(serviceDto.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to save GovService: ${e.message}")
        }
    }

    fun update(id: Long, serviceDto: ServiceDto): ServiceDto {
        serviceRepository.findById(id)
            .orElseThrow { NotFoundException("GovService with ID $id not found") }

        return try {
            serviceRepository.save(serviceDto.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to update GovService: ${e.message}")
        }
    }

    fun delete(id: Long) {
        val existingService = serviceRepository.findById(id)
            .orElseThrow { NotFoundException("GovService with ID $id not found") }

        try {
            serviceRepository.delete(existingService)
        } catch (e: Exception) {
            throw DataFormatException("Failed to delete GovService: ${e.message}")
        }
    }

    private fun buildSpecification(filters: List<FilterCriteria>): Specification<GovService> {
        return Specification { root, _, cb ->
            filters.mapNotNull { filter ->
                when (filter.field) {
                    "name" -> cb.like(root.get("name"), "%${filter.value}%")
                    "description" -> cb.like(root.get("description"), "%${filter.value}%")
                    "categoryId" -> cb.equal(root.get<Category>("category").get<Long>("id"), filter.value.toLong())
                    else -> null
                }
            }.reduce { a, b -> cb.and(a, b) }
        }
    }

    private fun GovService.toDto(): ServiceDto = ServiceDto(
        id = this.id,
        name = this.name ?: throw IllegalStateException("Name must not be null"),
        description = this.description ?: throw IllegalStateException("Description must not be null"),
        categoryId = this.category?.id ?: throw IllegalStateException("Category must not be null"),
        establishmentId = this.establishment?.id ?: throw IllegalStateException("Establishment must not be null")
    )

    private fun ServiceDto.toEntity(): GovService = GovService(
        id = this.id,
        name = this.name,
        description = this.description,
        category = Category(id = this.categoryId), // Создаем сущность Category из DTO
        establishment = Establishment(id = this.establishmentId) // Пример преобразования для establishment
    )
}


