package by.course.govservices.service

import by.course.govservices.dto.CategoryDto
import by.course.govservices.entities.Category
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.CategoryRepository
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    fun findAll(
        pagination: PaginationRequest,
        filters: List<FilterCriteria>
    ): Page<CategoryDto> {
        val pageRequest = PageRequest.of(pagination.page, pagination.size)

        val specification = buildSpecification(filters)

        val page = categoryRepository.findAll(specification, pageRequest)
        if (page.isEmpty) {
            throw NotFoundException("Categories not found")
        }

        return page.map { it.toDto() }
    }


    fun findById(id: Long): CategoryDto {
        return categoryRepository.findById(id)
            .map { it.toDto() }
            .orElseThrow { NotFoundException("Category with ID $id not found") }
    }

     fun save(entity: CategoryDto): CategoryDto {
        return try {
            categoryRepository.save(entity.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to save category: ${e.message}")
        }
    }

     fun update(id: Long, entity: CategoryDto): CategoryDto {
        val existingCategory = categoryRepository.findById(id)
            .orElseThrow { NotFoundException("Category with ID $id not found") }

        return try {
            categoryRepository.save(entity.toEntity()).toDto()
        } catch (e: Exception) {
            throw DataFormatException("Failed to update category: ${e.message}")
        }
    }

     fun delete(id: Long) {
        val existingCategory = categoryRepository.findById(id)
            .orElseThrow { NotFoundException("Category with ID $id not found") }

        try {
            categoryRepository.delete(existingCategory)
        } catch (e: Exception) {
            throw DataFormatException("Failed to delete category: ${e.message}")
        }
    }

    private fun buildSpecification(filters: List<FilterCriteria>): Specification<Category> {
        return Specification { root, query, cb ->
            filters.mapNotNull { filter ->
                when (filter.field) {
                    "category" -> cb.equal(root.get<String>("category"), filter.value)
                    else -> null
                }
            }.reduce(cb::and)
        }
    }
}

// Преобразование Category в DTO и наоборот
fun Category.toDto(): CategoryDto = CategoryDto(id, category)
fun CategoryDto.toEntity(): Category = Category(id, category)
