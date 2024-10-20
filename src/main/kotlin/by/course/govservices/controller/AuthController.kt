package by.course.govservices.controller

import by.course.govservices.dto.LoginRequest
import by.course.govservices.dto.UserRegisterDto

import by.course.govservices.service.IAuthService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@CrossOrigin(origins = arrayOf("*"))
@RequestMapping("/api/auth")
class AuthController(
    private val authService: IAuthService
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/register")
    fun register(@RequestBody userRegisterDto: UserRegisterDto): Mono<ResponseEntity<String>> {
        return authService.register(userRegisterDto)
            .doOnSuccess { logger.info("User registered: ${userRegisterDto.identifyNumber}") }
            .then(Mono.just(ResponseEntity.ok("User registered successfully")))
            .onErrorResume { ex -> // Обработка ошибки
                logger.error("Registration failed: ${ex.message}")
                Mono.just(ResponseEntity.badRequest().body("Registration failed"))
            }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): Mono<String> {
        return authService.login(loginRequest)
    }
}
