package by.course.govservices.service

import by.course.govservices.dto.BidStatusDto
import by.course.govservices.entities.BidStatus
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.BidStatusRepository
import by.course.govservices.service.base.BaseService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BidStatusService(
    private val bidStatusRepository: BidStatusRepository
) : BaseService<BidStatusDto, Int> {

    override fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Flux<BidStatusDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)

        // Строим динамическую строку для фильтров
        val whereClause = buildDynamicFilterClause(filters)

        return bidStatusRepository.findByDynamicFilter(whereClause)
            .skip(pageRequest.pageNumber * pageRequest.pageSize.toLong()) // Пропускаем нужное количество записей
            .take(pageRequest.pageSize.toLong()) // Берем только нужное количество записей
            .map { it.toDto() } // Преобразуем в BidStatusDto
            .switchIfEmpty(Mono.error(NotFoundException("BidStatuses not found"))) // Если пусто, выбрасываем ошибку
    }

    override fun findById(id: Int): Mono<BidStatusDto> {
        return bidStatusRepository.findById(id)
            .map { it.toDto() } // Преобразуем в BidStatusDto
            .switchIfEmpty(Mono.error(NotFoundException("BidStatus with ID $id not found"))) // Если не найдено, выбрасываем ошибку
    }

    override fun save(entity: BidStatusDto): Mono<BidStatusDto> {
        return bidStatusRepository.save(entity.toEntity()) // Сохраняем как сущность
            .map { it.toDto() } // Преобразуем в BidStatusDto
            .onErrorMap { e -> DataFormatException("Failed to save bidStatus: ${e.message}") }
    }

    override fun update(id: Int, entity: BidStatusDto): Mono<BidStatusDto> {
        return bidStatusRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("BidStatus with ID $id not found"))) // Проверка на существование
            .flatMap { bidStatusRepository.save(entity.toEntity()) } // Обновляем сущность
            .map { it.toDto() } // Преобразуем в BidStatusDto
            .onErrorMap { e -> DataFormatException("Failed to update bidStatus: ${e.message}") }
    }

    override fun delete(id: Int): Mono<Void> {
        return bidStatusRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("BidStatus with ID $id not found"))) // Проверка на существование
            .flatMap { bidStatusRepository.delete(it) } // Удаляем
            .onErrorMap { e -> DataFormatException("Failed to delete bidStatus: ${e.message}") }
    }

    // Метод для построения динамической строки фильтров для SQL-запроса
    private fun buildDynamicFilterClause(filters: List<FilterCriteria>): String {
        val filterClauses = mutableListOf<String>()

        filters.forEach { filter ->
            when (filter.field) {
                "statusName" -> filterClauses.add("status_name LIKE '%${filter.value}%'")
                "statusCode" -> filterClauses.add("status_code LIKE '%${filter.value}%'")
                else -> {} // Пропускаем неизвестные фильтры
            }
        }

        return filterClauses.joinToString(" AND ")
    }

    // Дополнительные методы для поиска статусов по имени и коду
    fun findByStatusName(statusName: String): Mono<BidStatusDto> {
        return bidStatusRepository.findByStatus(statusName)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("BidStatus with name $statusName not found")))
    }
}

fun BidStatus.toDto(): BidStatusDto {
    return BidStatusDto(
        id = this.id,
        status = this.status
    )
}

fun BidStatusDto.toEntity(): BidStatus {
    return BidStatus(
        id = this.id,
        status = this.status
    )
}
