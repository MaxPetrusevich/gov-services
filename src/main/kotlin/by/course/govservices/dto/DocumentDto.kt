package by.course.govservices.dto

import java.time.LocalDate

data class DocumentDto(
    val id: Long?,
    val link: String,
    val typeId: Long,
    val loadingDate: LocalDate?,
    val bidId: Long?
)