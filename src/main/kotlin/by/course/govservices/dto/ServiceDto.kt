package by.course.govservices.dto

data class ServiceDto(
    val id: Long?,
    val name: String,
    val description: String,
    val categoryId: Long,      // DTO для категории
    val establishmentId: Long // DTO для учреждения
)