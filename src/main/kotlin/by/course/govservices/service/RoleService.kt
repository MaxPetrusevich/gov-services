package by.course.govservices.service

import by.course.govservices.dto.RoleDto
import by.course.govservices.entities.Role
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.RoleRepository
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {

    /**
     * Получение всех ролей с фильтрацией и пагинацией.
     */
    fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Page<RoleDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)
        val specification = buildSpecification(filters)

        val roles = roleRepository.findAll(specification, pageRequest)
        if (roles.isEmpty) {
            throw NotFoundException("Roles not found")
        }

        return roles.map { it.toDto() }
    }

    /**
     * Получение роли по ID.
     */
    fun findById(id: Long): RoleDto {
        val role = roleRepository.findById(id)
            .orElseThrow { NotFoundException("Role with ID $id not found") }
        return role.toDto()
    }

    /**
     * Создание новой роли.
     */
    fun save(dto: RoleDto): RoleDto {
        return try {
            roleRepository.save(dto.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to save role: ${e.message}")
        }
    }

    /**
     * Обновление существующей роли.
     */
    fun update(id: Long, dto: RoleDto): RoleDto {
        val existingRole = roleRepository.findById(id)
            .orElseThrow { NotFoundException("Role with ID $id not found") }

        return try {
            roleRepository.save(dto.toEntity(existingRole)).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to update role: ${e.message}")
        }
    }

    /**
     * Удаление роли по ID.
     */
    fun delete(id: Long) {
        val role = roleRepository.findById(id)
            .orElseThrow { NotFoundException("Role with ID $id not found") }

        try {
            roleRepository.delete(role)
        } catch (e: Exception) {
            throw DataFormatException("Failed to delete role: ${e.message}")
        }
    }

    /**
     * Создание спецификации для фильтров.
     */
    private fun buildSpecification(filters: List<FilterCriteria>): Specification<Role> {
        return Specification { root, _, cb ->
            filters.mapNotNull { filter ->
                when (filter.field) {
                    "role" -> cb.like(cb.lower(root.get("roleName")), "%${filter.value.lowercase()}%")
                    else -> null
                }
            }.reduceOrNull { a, b -> cb.and(a, b) }
        }
    }
}
// Преобразование Role в DTO
fun Role.toDto(): RoleDto = RoleDto(
    id = this.id,
    role = this.roleName ?: throw IllegalStateException("Role name must not be null")
)

// Преобразование RoleDto в сущность
fun RoleDto.toEntity(existingEntity: Role? = null): Role = Role(
    id = existingEntity?.id ?: this.id,
    roleName = this.role
)
