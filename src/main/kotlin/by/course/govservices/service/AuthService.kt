package by.course.govservices.service

import by.course.govservices.config.security.IJwtTokenProvider
import by.course.govservices.dto.LoginRequest
import by.course.govservices.dto.UserRegisterDto
import by.course.govservices.entities.Citizen
import by.course.govservices.entities.User
import by.course.govservices.exceptions.InvalidCredentialsException
import by.course.govservices.exceptions.UserNotFoundException
import by.course.govservices.repositories.postgres.CitizenRepository
import by.course.govservices.repositories.postgres.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val citizenRepository: CitizenRepository,
    private val jwtTokenProvider: IJwtTokenProvider
) : IAuthService {

    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    override fun register(userRegisterDto: UserRegisterDto): Mono<Map<String, Any>> {
        return citizenRepository.findByIdentifyNumber(userRegisterDto.identifyNumber)
            .flatMap { existingCitizen ->
                // Возвращаем ошибку с нужным типом
                Mono.error<Map<String, Any>>(Exception("Citizen with identify number ${existingCitizen.identifyNumber} already exists"))
            }
            .switchIfEmpty(
                // Если гражданина нет, создаем нового пользователя и гражданина
                createUserAndCitizen(userRegisterDto)
            )
    }



    private fun createUserAndCitizen(userRegisterDto: UserRegisterDto): Mono<Map<String, Any>>  {
        val passwordEncoder = BCryptPasswordEncoder()
        val hashedPassword = passwordEncoder.encode(userRegisterDto.password)

        val user = User(
            identifyNumber = userRegisterDto.identifyNumber,
            password = hashedPassword,
            roleId = userRegisterDto.roleId // Убедитесь, что поле roleId присутствует в User
        )

        return userRepository.save(user)
            .flatMap { savedUser ->
                createCitizen(userRegisterDto, savedUser.id!!)
                    .then(Mono.just(savedUser))
            }
            .map { savedUser ->
                // Генерируем токен
                val token = jwtTokenProvider.generateToken(savedUser)
                mapOf("user" to savedUser, "token" to token) // Возвращаем пользователя и токен
            }
            .doOnSuccess { logger.info("User registered: ${it["user"]}") }
            .doOnError { e -> logger.error("Error registering user: ${e.message}", e) }
    }

    private fun createCitizen(userRegisterDto: UserRegisterDto, userId: Int): Mono<Citizen> { // Измените Int на Long, если id - Long
        val citizen = Citizen(
            firstName = userRegisterDto.firstName,
            lastName = userRegisterDto.lastName,
            middleName = userRegisterDto.middleName,
            phone = userRegisterDto.phone,
            email = userRegisterDto.email,
            identifyNumber = userRegisterDto.identifyNumber,
            passportSeries = userRegisterDto.passportSeries,
            passportNumber = userRegisterDto.passportNumber,
            address = userRegisterDto.address,
            userId = userId // Связываем гражданина с пользователем по идентификатору
        )

        return citizenRepository.save(citizen)
            .doOnSuccess { logger.info("Citizen registered: ${it.identifyNumber}") }
            .doOnError { e -> logger.error("Error registering citizen: ${e.message}", e) }
    }

    override  fun login(loginRequest: LoginRequest): Mono<String> {
        val passwordEncoder = BCryptPasswordEncoder()
        return userRepository.findByIdentifyNumber(loginRequest.identifyNumber)
            .flatMap { user ->
                // Проверяем, правильный ли пароль
                if (passwordEncoder.matches(loginRequest.password, user.password)) {
                    // Если пароль верный, генерируем токен
                    Mono.just(jwtTokenProvider.generateToken(user))
                } else {
                    Mono.error(InvalidCredentialsException("Неверные учетные данные"))
                }
            }
            .switchIfEmpty(Mono.error(UserNotFoundException("Пользователь не найден")))
    }
}