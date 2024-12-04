package by.course.govservices.controller

import by.course.govservices.dto.UserDto
import by.course.govservices.service.UserService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    // Получение всех пользователей с фильтрацией и пагинацией
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllUsers(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam(required = false) identifyNumber: String? = null,
        @RequestParam(required = false) roleId: Long? = null
    ): List<UserDto> {
        val filters = mutableListOf<FilterCriteria>()
        identifyNumber?.let { filters.add(FilterCriteria("identifyNumber", it)) }
        roleId?.let { filters.add(FilterCriteria("roleId", it.toString())) }

        val pagination = PaginationRequest(page, size)
        return userService.findAll(pagination, filters).content  // Возвращаем только содержимое страницы
    }

    // Получение пользователя по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getUserById(@PathVariable id: Long): UserDto {
        return userService.findById(id)
    }

    // Создание нового пользователя
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody userDto: UserDto): UserDto {
        return userService.save(userDto)
    }

    // Обновление существующего пользователя
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody userDto: UserDto
    ): UserDto {
        return userService.update(id, userDto)
    }

    // Удаление пользователя
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Long) {
        userService.delete(id)
    }
}
