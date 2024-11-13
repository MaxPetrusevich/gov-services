package by.course.govservices.dto

import java.math.BigDecimal
import java.time.LocalDate

data class PaymentDto(
    val id: Int?,
    val bidId: Int,
    val sum: BigDecimal,
    val date: LocalDate,
    val statusId: Int
)
