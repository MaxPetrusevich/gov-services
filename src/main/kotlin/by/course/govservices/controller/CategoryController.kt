package by.course.govservices.controller

import by.course.govservices.dto.CategoryDto
import by.course.govservices.service.CategoryService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/categories")
class CategoryController(private val categoryService: CategoryService) {

    // Получение категории по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getCategoryById(@PathVariable id: Long): CategoryDto {
        return categoryService.findById(id)
    }

    // Получение всех категорий с фильтрами и пагинацией
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCategories(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam(required = false) category: String? = null
    ): List<CategoryDto> {
        val filters = mutableListOf<FilterCriteria>()
        category?.let { filters.add(FilterCriteria("category", it)) }

        val pagination = PaginationRequest(page, size)
        return categoryService.findAll(pagination, filters).content // Возвращаем content из Page
    }

    // Создание новой категории
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(@RequestBody categoryDto: CategoryDto): CategoryDto {
        return categoryService.save(categoryDto)
    }

    // Обновление существующей категории
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCategory(@PathVariable id: Long, @RequestBody categoryDto: CategoryDto): CategoryDto {
        return categoryService.update(id, categoryDto)
    }

    // Удаление категории
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCategory(@PathVariable id: Long) {
        categoryService.delete(id)
    }
}
