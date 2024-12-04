package by.course.govservices.service.auth

import by.course.govservices.dto.LoginRequest
import by.course.govservices.dto.UserRegisterDto

interface IAuthService {
    fun register(userRegisterDto: UserRegisterDto): Map<String, Any>
    fun login(loginRequest: LoginRequest): String
}
