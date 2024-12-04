package by.course.govservices.dto

import java.math.BigDecimal
import java.time.LocalDate

data class PaymentDto(
    val id: Long?,
    val bidId: Long,
    val sum: BigDecimal,
    val date: LocalDate,
    val statusId: Long
)
