package by.course.govservices.service.auth

import by.course.govservices.dto.LoginRequest
import by.course.govservices.dto.UserRegisterDto
import reactor.core.publisher.Mono

interface IAuthService {
    fun register(userRegisterDto: UserRegisterDto):Mono<Map<String, Any>>
    fun login(loginRequest: LoginRequest): Mono<String>
}