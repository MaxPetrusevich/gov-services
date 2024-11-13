package by.course.govservices.service

import by.course.govservices.dto.BidDto
import by.course.govservices.entities.Bid
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.BidRepository
import by.course.govservices.service.base.BaseService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BidService(
    private val bidRepository: BidRepository
) : BaseService<BidDto, Long> {

    override fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Flux<BidDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)

        // Формируем строку с фильтрами
        val whereClause = buildWhereClause(filters)

        // Пагинация добавляется к запросу через параметр `with(pageRequest)`
        return bidRepository.findAllWithFilters(whereClause)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Bids not found")))
    }

    override fun findById(id: Long): Mono<BidDto> {
        return bidRepository.findById(id)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Bid with ID $id not found")))
    }

    override fun save(entity: BidDto): Mono<BidDto> {
        return bidRepository.save(entity.toEntity())
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to save bid: ${e.message}") }
    }

    override fun update(id: Long, entity: BidDto): Mono<BidDto> {
        return bidRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Bid with ID $id not found")))
            .flatMap { bidRepository.save(entity.toEntity()) }
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to update bid: ${e.message}") }
    }

    override fun delete(id: Long): Mono<Void> {
        return bidRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Bid with ID $id not found")))
            .flatMap { bidRepository.delete(it) }
            .onErrorMap { e -> DataFormatException("Failed to delete bid: ${e.message}") }
    }

    // Метод для построения строки WHERE с фильтрами
    private fun buildWhereClause(filters: List<FilterCriteria>): String {
        val whereConditions = filters.mapNotNull { filter ->
            when (filter.field) {
                "citizenId" -> "citizen_id = ${filter.value}"
                "serviceId" -> "service_id = ${filter.value}"
                "date" -> "date = '${filter.value}'"
                "statusId" -> "status_id = ${filter.value}"
                else -> null
            }
        }

        return if (whereConditions.isNotEmpty()) {
            whereConditions.joinToString(" AND ")
        } else {
            ""
        }
    }

    // Дополнительные методы для поиска заявок по гражданину и статусу
    fun findByCitizenId(citizenId: Long): Flux<BidDto> {
        return bidRepository.findByCitizenId(citizenId)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Bid for citizen with ID $citizenId not found")))
    }
}

// Преобразование Bid в DTO и наоборот
fun Bid.toDto(): BidDto = BidDto(id, citizenId, serviceId, date, statusId)
fun BidDto.toEntity(): Bid = Bid(id, citizenId, serviceId, date, statusId)
