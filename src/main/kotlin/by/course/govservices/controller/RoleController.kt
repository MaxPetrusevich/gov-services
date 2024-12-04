package by.course.govservices.controller

import by.course.govservices.dto.RoleDto
import by.course.govservices.service.RoleService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/roles")
class RoleController(private val roleService: RoleService) {

    // Получение всех ролей с фильтрацией и пагинацией
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllRoles(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam(required = false) role: String? = null
    ): List<RoleDto> {
        val filters = mutableListOf<FilterCriteria>()
        role?.let { filters.add(FilterCriteria("role", it)) }

        val pagination = PaginationRequest(page, size)
        return roleService.findAll(pagination, filters).content  // Возвращаем только содержимое страницы
    }

    // Получение роли по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getRoleById(@PathVariable id: Long): RoleDto {
        return roleService.findById(id)
    }

    // Создание новой роли
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createRole(@RequestBody roleDto: RoleDto): RoleDto {
        return roleService.save(roleDto)
    }

    // Обновление существующей роли
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateRole(
        @PathVariable id: Long,
        @RequestBody roleDto: RoleDto
    ): RoleDto {
        return roleService.update(id, roleDto)
    }

    // Удаление роли
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRole(@PathVariable id: Long) {
        roleService.delete(id)
    }
}
