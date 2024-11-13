package by.course.govservices.dto

import java.time.LocalDate

data class DocumentDto(
    val id: Int?,
    val link: String,
    val typeId: Int,
    val loadingDate: LocalDate?,
    val bidId: Int?
)