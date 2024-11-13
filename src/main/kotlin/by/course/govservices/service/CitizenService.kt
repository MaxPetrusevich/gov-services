package by.course.govservices.service

import by.course.govservices.dto.CitizenDto
import by.course.govservices.entities.Citizen
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.CitizenRepository
import by.course.govservices.service.base.BaseService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CitizenService(
    private val citizenRepository: CitizenRepository
) : BaseService<CitizenDto, Int> {  // Используем Int вместо Long

    override fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Flux<CitizenDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)

        // Строим динамическую строку для фильтров
        val whereClause = buildDynamicFilterClause(filters)

        return citizenRepository.findByDynamicFilter(whereClause)
            .skip(pageRequest.pageNumber * pageRequest.pageSize.toLong()) // Пропускаем нужное количество записей
            .take(pageRequest.pageSize.toLong()) // Берем только нужное количество записей
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Citizens not found")))
    }

    override fun findById(id: Int): Mono<CitizenDto> {  // Используем Int вместо Long
        return citizenRepository.findById(id)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Citizen with ID $id not found")))
    }

    override fun save(entity: CitizenDto): Mono<CitizenDto> {
        return citizenRepository.save(entity.toEntity())
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to save citizen: ${e.message}") }
    }

    override fun update(id: Int, entity: CitizenDto): Mono<CitizenDto> {  // Используем Int вместо Long
        return citizenRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Citizen with ID $id not found")))
            .flatMap { citizenRepository.save(entity.toEntity()) }
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to update citizen: ${e.message}") }
    }

    override fun delete(id: Int): Mono<Void> {  // Используем Int вместо Long
        return citizenRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Citizen with ID $id not found")))
            .flatMap { citizenRepository.delete(it) }
            .onErrorMap { e -> DataFormatException("Failed to delete citizen: ${e.message}") }
    }

    // Метод для построения динамической строки фильтров для SQL-запроса
    private fun buildDynamicFilterClause(filters: List<FilterCriteria>): String {
        val filterClauses = mutableListOf<String>()

        filters.forEach { filter ->
            when (filter.field) {
                "firstName" -> filterClauses.add("first_name LIKE '%${filter.value}%'")
                "lastName" -> filterClauses.add("last_name LIKE '%${filter.value}%'")
                "identifyNumber" -> filterClauses.add("identify_number LIKE '%${filter.value}%'")
                "address" -> filterClauses.add("address LIKE '%${filter.value}%'")
                "email" -> filterClauses.add("email LIKE '%${filter.value}%'")
                else -> {} // Пропускаем неизвестные фильтры
            }
        }

        return filterClauses.joinToString(" AND ")
    }

    // Дополнительные методы для поиска граждан по имени и идентификационному номеру
    fun findByFirstName(name: String): Mono<CitizenDto> {
        return citizenRepository.findByFirstName(name)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Citizen with name $name not found")))
    }

    fun findByIdentifyNumber(identifier: String): Mono<CitizenDto> {
        return citizenRepository.findCitizenByIdentifyNumber(identifier)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Citizen with identifier $identifier not found")))
    }
}

// Преобразование Citizen в DTO и наоборот
fun Citizen.toDto(): CitizenDto = CitizenDto(id, firstName, lastName, middleName, phone, email, identifyNumber, passportSeries, passportNumber, address, userId)
fun CitizenDto.toEntity(): Citizen = Citizen(id, firstName, lastName, middleName, phone, email, identifyNumber, passportSeries, passportNumber, address, userId)
