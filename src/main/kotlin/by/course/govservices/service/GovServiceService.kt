package by.course.govservices.service

import by.course.govservices.dto.ServiceDto
import by.course.govservices.entities.GovService
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.ServiceRepository
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class GovServiceService(
    private val serviceRepository: ServiceRepository
) {

    // Метод для поиска всех сервисов с динамическими фильтрами
    fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Flux<ServiceDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)

        // Строим динамическую строку для фильтров
        val whereClause = buildDynamicFilterClause(filters)

        return serviceRepository.findByDynamicFilter(whereClause)
            .skip(pageRequest.pageNumber * pageRequest.pageSize.toLong()) // Пропускаем нужное количество записей
            .take(pageRequest.pageSize.toLong()) // Берем только нужное количество записей
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("GovServices not found")))
    }

    // Метод для поиска сервиса по имени
    fun findByName(name: String): Mono<ServiceDto> {
        return serviceRepository.findByName(name)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("GovService with name $name not found")))
    }

    // Метод для сохранения нового сервиса
    fun save(serviceDto: ServiceDto): Mono<ServiceDto> {
        return serviceRepository.save(serviceDto.toEntity())
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to save GovService: ${e.message}") }
    }

    // Метод для обновления сервиса по ID
    fun update(id: Int, serviceDto: ServiceDto): Mono<ServiceDto> {
        return serviceRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("GovService with ID $id not found")))
            .flatMap { serviceRepository.save(serviceDto.toEntity()) }
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to update GovService: ${e.message}") }
    }

    // Метод для удаления сервиса по ID
    fun delete(id: Int): Mono<Void> {
        return serviceRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("GovService with ID $id not found")))
            .flatMap { serviceRepository.delete(it) }
            .onErrorMap { e -> DataFormatException("Failed to delete GovService: ${e.message}") }
    }

    // Метод для построения динамической строки фильтров для SQL-запроса
    private fun buildDynamicFilterClause(filters: List<FilterCriteria>): String {
        val filterClauses = mutableListOf<String>()

        filters.forEach { filter ->
            when (filter.field) {
                "name" -> filterClauses.add("name LIKE '%${filter.value}%'")
                "description" -> filterClauses.add("description LIKE '%${filter.value}%'")
                "categoryId" -> filterClauses.add("category_id = '${filter.value}'")
                else -> {} // Пропускаем неизвестные фильтры
            }
        }

        return filterClauses.joinToString(" AND ")
    }

    // Преобразование GovService в ServiceDto
    private fun GovService.toDto(): ServiceDto = ServiceDto(id, name, description, categoryId)

    // Преобразование ServiceDto в сущность GovService
    private fun ServiceDto.toEntity(): GovService = GovService(id, name, description, categoryId)
}
