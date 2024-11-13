package by.course.govservices.dto

import java.time.LocalDate

data class BidDto(
    val id: Int?,
    val citizenId: Int,
    val serviceId: Int,
    val date: LocalDate,
    val statusId: Int
)
