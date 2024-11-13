package by.course.govservices.service

import by.course.govservices.dto.PaymentStatusDto
import by.course.govservices.entities.PaymentStatus
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.PaymentStatusRepository
import by.course.govservices.service.base.BaseService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@Service
class PaymentStatusService(
    private val paymentStatusRepository: PaymentStatusRepository
) : BaseService<PaymentStatusDto, Long> {

    override fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Flux<PaymentStatusDto> {

        val statusFilter = filters.find { it.field == "status" }?.value ?: ""

        return paymentStatusRepository.findAllByStatusContainingIgnoreCase(statusFilter)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Payment statuses not found")))
    }

    override fun findById(id: Long): Mono<PaymentStatusDto> {
        return paymentStatusRepository.findById(id)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Payment status with ID $id not found")))
    }

    override fun save(entity: PaymentStatusDto): Mono<PaymentStatusDto> {
        return paymentStatusRepository.save(entity.toEntity())
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to save payment status: ${e.message}") }
    }

    override fun update(id: Long, entity: PaymentStatusDto): Mono<PaymentStatusDto> {
        return paymentStatusRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Payment status with ID $id not found")))
            .flatMap { paymentStatusRepository.save(entity.toEntity()) }
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to update payment status: ${e.message}") }
    }

    override fun delete(id: Long): Mono<Void> {
        return paymentStatusRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Payment status with ID $id not found")))
            .flatMap { paymentStatusRepository.delete(it) }
            .onErrorMap { e -> DataFormatException("Failed to delete payment status: ${e.message}") }
    }
}

// Преобразование PaymentStatus в DTO и наоборот
fun PaymentStatus.toDto(): PaymentStatusDto = PaymentStatusDto(id, status)
fun PaymentStatusDto.toEntity(): PaymentStatus = PaymentStatus(id, status)
