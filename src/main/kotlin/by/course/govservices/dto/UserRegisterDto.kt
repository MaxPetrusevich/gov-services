package by.course.govservices.dto

import java.time.LocalDate

data class UserRegisterDto(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val identifyNumber: String, // Обязательно к заполнению
    val passportSeries: String? = null,
    val passportNumber: String? = null,
    val address: String, // Обязательно к заполнению
    val password: String,
    val roleId: Int
)
