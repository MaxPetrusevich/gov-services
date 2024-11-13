package by.course.govservices.service

import by.course.govservices.dto.CategoryDto
import by.course.govservices.entities.Category
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.CategoryRepository
import by.course.govservices.service.base.BaseService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) : BaseService<CategoryDto, Int> {

    override fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Flux<CategoryDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)

        // Формируем строку с фильтрами
        val whereClause = buildWhereClause(filters)

        // Пагинация добавляется к запросу через параметр `with(pageRequest)`
        return categoryRepository.findAllWithFilters(whereClause)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Categories not found")))
    }

    override fun findById(id: Int): Mono<CategoryDto> {
        return categoryRepository.findById(id)
            .map { it.toDto() }
            .switchIfEmpty(Mono.error(NotFoundException("Category with ID $id not found")))
    }

    override fun save(entity: CategoryDto): Mono<CategoryDto> {
        return categoryRepository.save(entity.toEntity())
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to save category: ${e.message}") }
    }

    override fun update(id: Int, entity: CategoryDto): Mono<CategoryDto> {
        return categoryRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Category with ID $id not found")))
            .flatMap { categoryRepository.save(entity.toEntity()) }
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to update category: ${e.message}") }
    }

    override fun delete(id: Int): Mono<Void> {
        return categoryRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Category with ID $id not found")))
            .flatMap { categoryRepository.delete(it) }
            .onErrorMap { e -> DataFormatException("Failed to delete category: ${e.message}") }
    }

    // Метод для построения строки WHERE с фильтрами
    private fun buildWhereClause(filters: List<FilterCriteria>): String {
        val whereConditions = filters.mapNotNull { filter ->
            when (filter.field) {
                "category" -> "category = '${filter.value}'"
                else -> null // Пропускаем неизвестные фильтры
            }
        }

        return if (whereConditions.isNotEmpty()) {
            whereConditions.joinToString(" AND ")
        } else {
            ""
        }
    }
}

// Преобразование Category в DTO и наоборот
fun Category.toDto(): CategoryDto = CategoryDto(id, category)
fun CategoryDto.toEntity(): Category = Category(id, category)
