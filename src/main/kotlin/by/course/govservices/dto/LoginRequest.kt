package by.course.govservices.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class LoginRequest(
    @field:NotBlank(message = "Идентификационный номер обязателен.")
    @field:Pattern(
        regexp = "^[0-9]{7}[A-Za-z0-9][0-9]{6}[A-Za-z0-9]$",
        message = "Некорректный формат идентификационного номера."
    )
    val identifyNumber: String,

    @field:NotBlank(message = "Пароль обязателен.")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$",
        message = "Пароль должен содержать минимум 8 символов, включая заглавные и строчные буквы, цифры и спецсимволы."
    )
    val password: String
)
