package by.course.govservices.dto

import java.time.LocalDate

data class BidDto(
    val id: Long?,
    val citizenId: Long?,
    val serviceId: Long?,
    val date: LocalDate,
    val statusId: Long?
)
