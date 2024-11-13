package by.course.govservices.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size


data class UserRegisterDto(
    @field:NotBlank(message = "Имя обязательно.")
    val firstName: String,

    @field:NotBlank(message = "Фамилия обязательна.")
    val lastName: String,

    val middleName: String? = null,

    @field:Pattern(
        regexp = "^\\+?[0-9]{10,15}$",
        message = "Некорректный номер телефона."
    )
    val phone: String? = null,

    @field:Email(message = "Некорректный формат email.")
    val email: String? = null,

    @field:Pattern(
        regexp = "^\\d{7}[0-9]\\d{6}[0-9]$",
        message = "Некорректный формат идентификационного номера."
    )
    val identifyNumber: String,

    @field:Size(min = 2, max = 2, message = "Серия паспорта должна содержать 2 символа.")
    val passportSeries: String? = null,

    @field:Size(min = 6, max = 6, message = "Номер паспорта должен содержать 6 цифр.")
    val passportNumber: String? = null,

    @field:NotBlank(message = "Адрес обязателен.")
    val address: String,

    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$",
        message = "Пароль должен содержать минимум 8 символов, включая заглавные и строчные буквы, цифры и спецсимволы."
    )
    val password: String,

    val roleId: Int
)
