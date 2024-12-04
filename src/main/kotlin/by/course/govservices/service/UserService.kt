package by.course.govservices.service

import by.course.govservices.dto.UserDto
import by.course.govservices.entities.User
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.UserRepository
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    /**
     * Получение всех пользователей с фильтрацией и пагинацией.
     */
    fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Page<UserDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)
        val specification = buildSpecification(filters)

        val users = userRepository.findAll(specification, pageRequest)
        if (users.isEmpty) {
            throw NotFoundException("Users not found")
        }

        return users.map { it.toDto() }
    }

    /**
     * Получение пользователя по ID.
     */
    fun findById(id: Long): UserDto {
        val user = userRepository.findById(id)
            .orElseThrow { NotFoundException("User with ID $id not found") }
        return user.toDto()
    }

    /**
     * Создание нового пользователя.
     */
    fun save(dto: UserDto): UserDto {
        val user = dto.toEntity()
        return try {
            userRepository.save(user).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to save user: ${e.message}")
        }
    }

    /**
     * Обновление существующего пользователя.
     */
    fun update(id: Long, dto: UserDto): UserDto {
        val existingUser = userRepository.findById(id)
            .orElseThrow { NotFoundException("User with ID $id not found") }

        val updatedUser = dto.toEntity(existingUser)
        return try {
            userRepository.save(updatedUser).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to update user: ${e.message}")
        }
    }

    /**
     * Удаление пользователя по ID.
     */
    fun delete(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { NotFoundException("User with ID $id not found") }

        try {
            userRepository.delete(user)
        } catch (e: Exception) {
            throw DataFormatException("Failed to delete user: ${e.message}")
        }
    }

    /**
     * Создание спецификации для фильтров.
     */
    private fun buildSpecification(filters: List<FilterCriteria>): Specification<User> {
        return Specification { root, _, cb ->
            filters.mapNotNull { filter ->
                when (filter.field) {
                    "identifyNumber" -> cb.like(cb.lower(root.get("identifyNumber")), "%${filter.value.lowercase()}%")
                    "roleId" -> cb.equal(root.get<Long>("role").get<Long>("id"), filter.value.toLong())
                    else -> null
                }
            }.reduceOrNull { a, b -> cb.and(a, b) }
        }
    }
}

// Преобразование User в UserDto
fun User.toDto(): UserDto = UserDto(
    id = this.id,
    identifyNumber = this.identifyNumber ?: throw IllegalStateException("Identify number must not be null"),
    roleId = this.role?.id ?: throw IllegalStateException("Role ID must not be null")
)

// Преобразование UserDto в User
fun UserDto.toEntity(existingEntity: User? = null): User = User(
    id = existingEntity?.id ?: this.id,
    identifyNumber = this.identifyNumber,
    password = existingEntity?.password, // Пароль из существующей сущности
    role = existingEntity?.role // Роль из существующей сущности
)
