package by.course.govservices.controller

import by.course.govservices.dto.TypeDto
import by.course.govservices.service.TypeService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/types")
class TypeController(private val typeService: TypeService) {

    // Получение всех типов с фильтрацией и пагинацией
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllTypes(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam(required = false) type: String? = null
    ): List<TypeDto> {
        val filters = mutableListOf<FilterCriteria>()
        type?.let { filters.add(FilterCriteria("type", it)) }

        val pagination = PaginationRequest(page, size)
        return typeService.findAll(pagination, filters).content  // Возвращаем только содержимое страницы
    }

    // Получение типа по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getTypeById(@PathVariable id: Long): TypeDto {
        return typeService.findById(id)
    }

    // Создание нового типа
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createType(@RequestBody typeDto: TypeDto): TypeDto {
        return typeService.save(typeDto)
    }

    // Обновление существующего типа
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateType(
        @PathVariable id: Long,
        @RequestBody typeDto: TypeDto
    ): TypeDto {
        return typeService.update(id, typeDto)
    }

    // Удаление типа
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteType(@PathVariable id: Long) {
        typeService.delete(id)
    }
}
