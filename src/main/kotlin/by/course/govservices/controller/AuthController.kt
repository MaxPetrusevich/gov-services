package by.course.govservices.controller

import by.course.govservices.dto.LoginRequest
import by.course.govservices.dto.UserRegisterDto
import by.course.govservices.service.auth.IAuthService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/api/auth")
class AuthController(
    private val authService: IAuthService
) {

    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/register")
    fun register(@RequestBody userRegisterDto: UserRegisterDto): ResponseEntity<String> {
        return try {
            authService.register(userRegisterDto)
            logger.info("User registered: ${userRegisterDto.identifyNumber}")
            ResponseEntity.ok("User registered successfully")
        } catch (ex: Exception) {
            logger.error("Registration failed: ${ex.message}")
            ResponseEntity.badRequest().body("Registration failed")
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): String {
        return authService.login(loginRequest)
    }
}
