package by.course.govservices.service

import by.course.govservices.dto.RoleDto
import by.course.govservices.entities.Role
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.RoleRepository
import by.course.govservices.service.base.BaseService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@Service
class RoleService(
    private val roleRepository: RoleRepository
) : BaseService<RoleDto, Long> {

    override fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Flux<RoleDto> {

        val roleFilter = filters.find { it.field == "role" }?.value ?: ""

        return roleRepository.findAllByRoleContainingIgnoreCase(roleFilter)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Roles not found")))
    }

    override fun findById(id: Long): Mono<RoleDto> {
        return roleRepository.findById(id)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Role with ID $id not found")))
    }

    override fun save(entity: RoleDto): Mono<RoleDto> {
        return roleRepository.save(entity.toEntity())
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to save role: ${e.message}") }
    }

    override fun update(id: Long, entity: RoleDto): Mono<RoleDto> {
        return roleRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Role with ID $id not found")))
            .flatMap { roleRepository.save(entity.toEntity()) }
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to update role: ${e.message}") }
    }

    override fun delete(id: Long): Mono<Void> {
        return roleRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Role with ID $id not found")))
            .flatMap { roleRepository.delete(it) }
            .onErrorMap { e -> DataFormatException("Failed to delete role: ${e.message}") }
    }
}

// Преобразование Role в DTO и наоборот
fun Role.toDto(): RoleDto = RoleDto(id, role)
fun RoleDto.toEntity(): Role = Role(id, role)
