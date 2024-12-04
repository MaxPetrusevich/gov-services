package by.course.govservices.service

import by.course.govservices.dto.PaymentDto
import by.course.govservices.entities.Bid
import by.course.govservices.entities.Payment
import by.course.govservices.entities.PaymentStatus
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.PaymentRepository
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository
) {

    /**
     * Получение всех платежей с учетом фильтров и пагинации.
     */
    fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Page<PaymentDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)
        val specification = buildSpecification(filters)

        val page = paymentRepository.findAll(specification, pageRequest)
        if (page.isEmpty) {
            throw NotFoundException("Payments not found")
        }

        return page.map { it.toDto() }
    }

    /**
     * Получение платежа по ID.
     */
    fun findById(id: Long): PaymentDto {
        val payment = paymentRepository.findById(id)
            .orElseThrow { NotFoundException("Payment with ID $id not found") }
        return payment.toDto()
    }

    /**
     * Создание нового платежа.
     */
    fun save(paymentDto: PaymentDto): PaymentDto {
        return try {
            paymentRepository.save(paymentDto.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to save payment: ${e.message}")
        }
    }

    /**
     * Обновление существующего платежа.
     */
    fun update(id: Long, paymentDto: PaymentDto): PaymentDto {
        val existingPayment = paymentRepository.findById(id)
            .orElseThrow { NotFoundException("Payment with ID $id not found") }

        return try {
            paymentRepository.save(paymentDto.toEntity(existingPayment)).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to update payment: ${e.message}")
        }
    }

    /**
     * Удаление платежа по ID.
     */
    fun delete(id: Long) {
        val existingPayment = paymentRepository.findById(id)
            .orElseThrow { NotFoundException("Payment with ID $id not found") }

        try {
            paymentRepository.delete(existingPayment)
        } catch (e: Exception) {
            throw DataFormatException("Failed to delete payment: ${e.message}")
        }
    }

    /**
     * Получение платежей по дате.
     */
    fun findByDate(date: LocalDate): List<PaymentDto> {
        val payments = paymentRepository.findByDate(date)
        if (payments.isEmpty()) {
            throw NotFoundException("Payments on date $date not found")
        }

        return payments.map { it.toDto() }
    }

    /**
     * Создание спецификации для фильтров.
     */
    private fun buildSpecification(filters: List<FilterCriteria>): Specification<Payment> {
        return Specification { root, _, cb ->
            filters.mapNotNull { filter ->
                when (filter.field) {
                    "bidId" -> cb.equal(root.get<Long>("bid").get<Long>("id"), filter.value.toLong())
                    "sum" -> cb.equal(root.get<BigDecimal>("sum"), filter.value.toBigDecimal())
                    "date" -> cb.equal(root.get<LocalDate>("date"), LocalDate.parse(filter.value))
                    "statusId" -> cb.equal(root.get<Long>("status").get<Long>("id"), filter.value.toLong())
                    else -> null
                }
            }.reduce { a, b -> cb.and(a, b) }
        }
    }
}
// Преобразование Payment в DTO
fun Payment.toDto(): PaymentDto = PaymentDto(
    id = this.id,
    bidId = this.bid?.id ?: throw IllegalStateException("Bid must not be null"),
    sum = this.sum ?: throw IllegalStateException("Sum must not be null"),
    date = this.date,
    statusId = this.status?.id ?: throw IllegalStateException("Status must not be null")
)

// Преобразование PaymentDto в сущность
fun PaymentDto.toEntity(existingPayment: Payment? = null): Payment = Payment(
    id = existingPayment?.id ?: this.id,
    bid = Bid(this.bidId), // Предполагаем, что только ID передается для Bid
    sum = this.sum,
    date = this.date,
    status = PaymentStatus(this.statusId) // Только ID для статуса
)


