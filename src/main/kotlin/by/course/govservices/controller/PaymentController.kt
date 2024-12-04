package by.course.govservices.controller

import by.course.govservices.dto.PaymentDto
import by.course.govservices.service.PaymentService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDate

@RestController
@RequestMapping("/api/payments")
class PaymentController(private val paymentService: PaymentService) {

    // Получение всех платежей с фильтрами и пагинацией
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllPayments(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam(required = false) bidId: Long? = null,
        @RequestParam(required = false) sum: BigDecimal? = null,
        @RequestParam(required = false) date: String? = null,
        @RequestParam(required = false) statusId: Long? = null
    ): List<PaymentDto> {
        val filters = mutableListOf<FilterCriteria>()
        bidId?.let { filters.add(FilterCriteria("bidId", it.toString())) }
        sum?.let { filters.add(FilterCriteria("sum", it.toString())) }
        date?.let { filters.add(FilterCriteria("date", it)) }
        statusId?.let { filters.add(FilterCriteria("statusId", it.toString())) }

        val pagination = PaginationRequest(page, size)
        return paymentService.findAll(pagination, filters).content  // Возвращаем только содержимое страницы
    }

    // Получение платежа по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPaymentById(@PathVariable id: Long): PaymentDto {
        return paymentService.findById(id)
    }

    // Создание нового платежа
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPayment(@RequestBody paymentDto: PaymentDto): PaymentDto {
        return paymentService.save(paymentDto)
    }

    // Обновление существующего платежа
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updatePayment(@PathVariable id: Long, @RequestBody paymentDto: PaymentDto): PaymentDto {
        return paymentService.update(id, paymentDto)
    }

    // Удаление платежа
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePayment(@PathVariable id: Long) {
        paymentService.delete(id)
    }

    // Получение платежей по дате
    @GetMapping("/date/{date}")
    @ResponseStatus(HttpStatus.OK)
    fun getPaymentsByDate(@PathVariable date: String): List<PaymentDto> {
        val localDate = LocalDate.parse(date)  // Преобразуем строку в LocalDate
        return paymentService.findByDate(localDate)
    }
}
