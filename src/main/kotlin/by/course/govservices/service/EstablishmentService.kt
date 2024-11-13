package by.course.govservices.service

import by.course.govservices.dto.EstablishmentDto
import by.course.govservices.entities.Establishment
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.EstablishmentRepository
import by.course.govservices.service.base.BaseService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class EstablishmentService(
    private val establishmentRepository: EstablishmentRepository
) : BaseService<EstablishmentDto, Int> {  // Используем Int для идентификаторов учреждений

    override fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Flux<EstablishmentDto> {
        // Строим динамическую строку для фильтров
        val whereClause = buildDynamicFilterClause(filters)

        return establishmentRepository.findByDynamicFilter(whereClause)
            .skip((pagination.page * pagination.size).toLong()) // Пропускаем записи на основе пагинации
            .take(pagination.size.toLong()) // Берем нужное количество записей
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Establishments not found")))
    }

    override fun findById(id: Int): Mono<EstablishmentDto> {  // Используем Int для идентификаторов
        return establishmentRepository.findById(id)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Establishment with ID $id not found")))
    }

    override fun save(entity: EstablishmentDto): Mono<EstablishmentDto> {
        return establishmentRepository.save(entity.toEntity())
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to save establishment: ${e.message}") }
    }

    override fun update(id: Int, entity: EstablishmentDto): Mono<EstablishmentDto> {  // Используем Int для идентификаторов
        return establishmentRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Establishment with ID $id not found")))
            .flatMap { establishmentRepository.save(entity.toEntity()) }
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to update establishment: ${e.message}") }
    }

    override fun delete(id: Int): Mono<Void> {  // Используем Int для идентификаторов
        return establishmentRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Establishment with ID $id not found")))
            .flatMap { establishmentRepository.delete(it) }
            .onErrorMap { e -> DataFormatException("Failed to delete establishment: ${e.message}") }
    }

    // Метод для построения динамической строки фильтров для SQL-запроса
    private fun buildDynamicFilterClause(filters: List<FilterCriteria>): String {
        val filterClauses = mutableListOf<String>()

        filters.forEach { filter ->
            when (filter.field) {
                "name" -> filterClauses.add("name LIKE '%${filter.value}%'")
                "address" -> filterClauses.add("address LIKE '%${filter.value}%'")
                "contact" -> filterClauses.add("contact LIKE '%${filter.value}%'")
                else -> {} // Пропускаем неизвестные фильтры
            }
        }

        return filterClauses.joinToString(" AND ")
    }
}

// Преобразование Establishment в DTO и наоборот
fun Establishment.toDto(): EstablishmentDto = EstablishmentDto(id, name)
fun EstablishmentDto.toEntity(): Establishment = Establishment(id, name)
