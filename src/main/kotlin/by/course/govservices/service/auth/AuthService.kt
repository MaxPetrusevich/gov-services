package by.course.govservices.service.auth

import by.course.govservices.config.security.IJwtTokenProvider
import by.course.govservices.dto.LoginRequest
import by.course.govservices.dto.UserRegisterDto
import by.course.govservices.entities.Citizen
import by.course.govservices.entities.User
import by.course.govservices.exceptions.InvalidCredentialsException
import by.course.govservices.exceptions.UserNotFoundException
import by.course.govservices.repositories.CitizenRepository
import by.course.govservices.repositories.RoleRepository
import by.course.govservices.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val citizenRepository: CitizenRepository,
    private val jwtTokenProvider: IJwtTokenProvider,
    private val roleRepository: RoleRepository
) : IAuthService {


    override fun register(userRegisterDto: UserRegisterDto): Map<String, Any> {
        val existingCitizen = citizenRepository.findByIdentifyNumber(userRegisterDto.identifyNumber)
        if (existingCitizen != null) {
            throw Exception("Citizen with identify number ${existingCitizen.identifyNumber} already exists")
        }

        return createUserAndCitizen(userRegisterDto)
    }

    private fun createUserAndCitizen(userRegisterDto: UserRegisterDto): Map<String, Any> {
        val passwordEncoder = BCryptPasswordEncoder()
        val hashedPassword = passwordEncoder.encode(userRegisterDto.password)

        val user = User(
            identifyNumber = userRegisterDto.identifyNumber,
            password = hashedPassword,
            role = roleRepository.findById(userRegisterDto.roleId).orElseThrow() // Убедитесь, что поле roleId присутствует в User
        )

        val savedUser = userRepository.save(user)
        val citizen = createCitizen(userRegisterDto, savedUser)

        // Генерируем токен
        val token = jwtTokenProvider.generateToken(savedUser)
        return mapOf("user" to savedUser, "token" to token)
    }

    private fun createCitizen(userRegisterDto: UserRegisterDto, user: User): Citizen {
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
            user = user // Связываем гражданина с пользователем по идентификатору
        )

        return citizenRepository.save(citizen)
    }

    override fun login(loginRequest: LoginRequest): String {
        val user = userRepository.findByIdentifyNumber(loginRequest.identifyNumber)
            ?: throw UserNotFoundException("Пользователь не найден")

        val passwordEncoder = BCryptPasswordEncoder()
        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw InvalidCredentialsException("Неверные учетные данные")
        }

        // Генерация токена
        return jwtTokenProvider.generateToken(user)
    }
}
