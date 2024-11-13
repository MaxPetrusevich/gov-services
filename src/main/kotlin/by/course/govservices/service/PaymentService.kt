package by.course.govservices.service

import by.course.govservices.dto.PaymentDto
import by.course.govservices.entities.Payment
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.PaymentRepository
import by.course.govservices.service.base.BaseService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository
) : BaseService<PaymentDto, Long> {

    override fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Flux<PaymentDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)

        // Строим динамическую строку для фильтров
        val whereClause = buildDynamicFilterClause(filters)

        return paymentRepository.findByDynamicFilter(whereClause)
            .skip(pageRequest.pageNumber * pageRequest.pageSize.toLong()) // Пропускаем нужное количество записей
            .take(pageRequest.pageSize.toLong()) // Берем только нужное количество записей
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Payments not found")))
    }

    override fun findById(id: Long): Mono<PaymentDto> {
        return paymentRepository.findById(id)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Payment with ID $id not found")))
    }

    override fun save(entity: PaymentDto): Mono<PaymentDto> {
        return paymentRepository.save(entity.toEntity())
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to save payment: ${e.message}") }
    }

    override fun update(id: Long, entity: PaymentDto): Mono<PaymentDto> {
        return paymentRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Payment with ID $id not found")))
            .flatMap { paymentRepository.save(entity.toEntity()) }
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to update payment: ${e.message}") }
    }

    override fun delete(id: Long): Mono<Void> {
        return paymentRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Payment with ID $id not found")))
            .flatMap { paymentRepository.delete(it) }
            .onErrorMap { e -> DataFormatException("Failed to delete payment: ${e.message}") }
    }

    // Метод для построения динамической строки фильтров для SQL-запроса
    private fun buildDynamicFilterClause(filters: List<FilterCriteria>): String {
        val filterClauses = mutableListOf<String>()

        filters.forEach { filter ->
            when (filter.field) {
                "bidId" -> filterClauses.add("bid_id LIKE '%${filter.value}%'")
                "sum" -> filterClauses.add("sum LIKE '%${filter.value}%'")
                "date" -> filterClauses.add("date = '${filter.value}'")
                "statusId" -> filterClauses.add("status_id = '${filter.value}'")
                else -> {} // Пропускаем неизвестные фильтры
            }
        }

        return filterClauses.joinToString(" AND ")
    }



    fun findByDate(date: LocalDate): Mono<PaymentDto> {
        return paymentRepository.findByDate(date)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Payment on date $date not found")))
    }
}

// Преобразование Payment в DTO и наоборот
fun Payment.toDto(): PaymentDto = PaymentDto(id, bidId, sum, date, statusId)
fun PaymentDto.toEntity(): Payment = Payment(id, bidId, sum, date, statusId)
