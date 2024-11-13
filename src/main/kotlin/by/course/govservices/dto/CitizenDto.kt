package by.course.govservices.dto

data class CitizenDto(
    val id: Int?,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val phone: String?,
    val email: String?,
    val identifyNumber: String,
    val passportSeries: String?,
    val passportNumber: String?,
    val address: String,
    val userId: Int
)