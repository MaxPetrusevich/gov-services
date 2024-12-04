package by.course.govservices.controller

import by.course.govservices.dto.PaymentStatusDto
import by.course.govservices.service.PaymentStatusService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment-statuses")
class PaymentStatusController(private val paymentStatusService: PaymentStatusService) {

    // Получение всех статусов с фильтрами и пагинацией
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllPaymentStatuses(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam(required = false) status: String? = null
    ): List<PaymentStatusDto> {
        val filters = mutableListOf<FilterCriteria>()
        status?.let { filters.add(FilterCriteria("status", it)) }

        val pagination = PaginationRequest(page, size)
        return paymentStatusService.findAll(pagination, filters).content  // Возвращаем только содержимое страницы
    }

    // Получение статуса по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPaymentStatusById(@PathVariable id: Long): PaymentStatusDto {
        return paymentStatusService.findById(id)
    }

    // Создание нового статуса
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPaymentStatus(@RequestBody paymentStatusDto: PaymentStatusDto): PaymentStatusDto {
        return paymentStatusService.save(paymentStatusDto)
    }

    // Обновление существующего статуса
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updatePaymentStatus(
        @PathVariable id: Long,
        @RequestBody paymentStatusDto: PaymentStatusDto
    ): PaymentStatusDto {
        return paymentStatusService.update(id, paymentStatusDto)
    }

    // Удаление статуса
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePaymentStatus(@PathVariable id: Long) {
        paymentStatusService.delete(id)
    }
}
