package by.course.govservices.service

import by.course.govservices.dto.CitizenDto
import by.course.govservices.entities.Citizen
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.CitizenRepository
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class CitizenService(
    private val citizenRepository: CitizenRepository
) {

    fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Page<CitizenDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)

        val specification = buildSpecification(filters)

        val page = citizenRepository.findAll(specification, pageRequest)
        if (page.isEmpty) {
            throw NotFoundException("Citizens not found")
        }

        return page.map { it.toDto() }
    }

    fun findById(id: Long): CitizenDto {
        return citizenRepository.findById(id)
            .map { it.toDto() }
            .orElseThrow { NotFoundException("Citizen with ID $id not found") }
    }

    fun save(entity: CitizenDto): CitizenDto {
        return try {
            citizenRepository.save(entity.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to save citizen: ${e.message}")
        }
    }

    fun update(id: Long, entity: CitizenDto): CitizenDto {
        citizenRepository.findById(id)
            .orElseThrow { NotFoundException("Citizen with ID $id not found") }

        return try {
            citizenRepository.save(entity.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to update citizen: ${e.message}")
        }
    }

    fun delete(id: Long) {
        val existingCitizen = citizenRepository.findById(id)
            .orElseThrow { NotFoundException("Citizen with ID $id not found") }

        try {
            citizenRepository.delete(existingCitizen)
        } catch (e: Exception) {
            throw DataFormatException("Failed to delete citizen: ${e.message}")
        }
    }

    private fun buildSpecification(filters: List<FilterCriteria>): Specification<Citizen> {
        return Specification { root, query, cb ->
            filters.mapNotNull { filter ->
                when (filter.field) {
                    "firstName" -> cb.like(root.get("firstName"), "%${filter.value}%")
                    "lastName" -> cb.like(root.get("lastName"), "%${filter.value}%")
                    "identifyNumber" -> cb.like(root.get("identifyNumber"), "%${filter.value}%")
                    "address" -> cb.like(root.get("address"), "%${filter.value}%")
                    "email" -> cb.like(root.get("email"), "%${filter.value}%")
                    else -> null
                }
            }.reduce(cb::and)
        }
    }
}
// Преобразование Citizen в DTO и наоборот
fun Citizen.toDto(): CitizenDto = CitizenDto(
    id = this.id,
    firstName = this.firstName ?: "",
    lastName = this.lastName ?: "",
    middleName = this.middleName,
    phone = this.phone,
    email = this.email,
    identifyNumber = this.identifyNumber ?: "",
    passportSeries = this.passportSeries,
    passportNumber = this.passportNumber,
    address = this.address ?: "",
    userId = this.user?.id?: 0
)

fun CitizenDto.toEntity(): Citizen = Citizen(
    id = this.id?.toLong(),
    firstName = this.firstName,
    lastName = this.lastName,
    middleName = this.middleName,
    phone = this.phone,
    email = this.email,
    identifyNumber = this.identifyNumber,
    passportSeries = this.passportSeries,
    passportNumber = this.passportNumber,
    address = this.address,
    user = null // Нужно устанавливать пользователя отдельно в сервисе, если требуется
)
