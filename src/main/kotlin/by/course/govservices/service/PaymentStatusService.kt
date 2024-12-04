package by.course.govservices.service

import by.course.govservices.dto.PaymentStatusDto
import by.course.govservices.entities.PaymentStatus
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.PaymentStatusRepository
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class PaymentStatusService(
    private val paymentStatusRepository: PaymentStatusRepository
) {

    /**
     * Получение всех статусов с учетом фильтров и пагинации.
     */
    fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Page<PaymentStatusDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)
        val specification = buildSpecification(filters)

        val statuses = paymentStatusRepository.findAll(specification, pageRequest)
        if (statuses.isEmpty) {
            throw NotFoundException("Payment statuses not found")
        }

        return statuses.map { it.toDto() }
    }

    /**
     * Получение статуса по ID.
     */
    fun findById(id: Long): PaymentStatusDto {
        val status = paymentStatusRepository.findById(id)
            .orElseThrow { NotFoundException("Payment status with ID $id not found") }
        return status.toDto()
    }

    /**
     * Создание нового статуса.
     */
    fun save(dto: PaymentStatusDto): PaymentStatusDto {
        return try {
            paymentStatusRepository.save(dto.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to save payment status: ${e.message}")
        }
    }

    /**
     * Обновление существующего статуса.
     */
    fun update(id: Long, dto: PaymentStatusDto): PaymentStatusDto {
        val existingStatus = paymentStatusRepository.findById(id)
            .orElseThrow { NotFoundException("Payment status with ID $id not found") }

        return try {
            paymentStatusRepository.save(dto.toEntity(existingStatus)).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to update payment status: ${e.message}")
        }
    }

    /**
     * Удаление статуса по ID.
     */
    fun delete(id: Long) {
        val status = paymentStatusRepository.findById(id)
            .orElseThrow { NotFoundException("Payment status with ID $id not found") }

        try {
            paymentStatusRepository.delete(status)
        } catch (e: Exception) {
            throw DataFormatException("Failed to delete payment status: ${e.message}")
        }
    }

    /**
     * Создание спецификации для фильтров.
     */
    private fun buildSpecification(filters: List<FilterCriteria>): Specification<PaymentStatus> {
        return Specification { root, _, cb ->
            filters.mapNotNull { filter ->
                when (filter.field) {
                    "status" -> cb.like(cb.lower(root.get("status")), "%${filter.value.lowercase()}%")
                    else -> null
                }
            }.reduceOrNull { a, b -> cb.and(a, b) }
        }
    }
}
// Преобразование PaymentStatus в DTO
fun PaymentStatus.toDto(): PaymentStatusDto = PaymentStatusDto(
    id = this.id,
    status = this.status ?: throw IllegalStateException("Status must not be null")
)

// Преобразование PaymentStatusDto в сущность
fun PaymentStatusDto.toEntity(existingEntity: PaymentStatus? = null): PaymentStatus = PaymentStatus(
    id = existingEntity?.id ?: this.id,
    status = this.status
)
