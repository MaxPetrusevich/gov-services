package by.course.govservices.controller

import by.course.govservices.dto.EstablishmentDto
import by.course.govservices.service.EstablishmentService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/establishments")
class EstablishmentController(private val establishmentService: EstablishmentService) {

    // Получение учреждения по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getEstablishmentById(@PathVariable id: Long): EstablishmentDto {
        return establishmentService.findById(id)
    }

    // Получение всех учреждений с фильтрами и пагинацией
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllEstablishments(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam(required = false) name: String? = null,
        @RequestParam(required = false) address: String? = null,
        @RequestParam(required = false) contact: String? = null
    ): List<EstablishmentDto> {
        val filters = mutableListOf<FilterCriteria>()
        name?.let { filters.add(FilterCriteria("name", it)) }
        address?.let { filters.add(FilterCriteria("address", it)) }
        contact?.let { filters.add(FilterCriteria("contact", it)) }

        val pagination = PaginationRequest(page, size)
        return establishmentService.findAll(pagination, filters).content  // Возвращаем только содержимое страницы
    }

    // Создание нового учреждения
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createEstablishment(@RequestBody establishmentDto: EstablishmentDto): EstablishmentDto {
        return establishmentService.save(establishmentDto)
    }

    // Обновление существующего учреждения
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateEstablishment(@PathVariable id: Long, @RequestBody establishmentDto: EstablishmentDto): EstablishmentDto {
        return establishmentService.update(id, establishmentDto)
    }

    // Удаление учреждения
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEstablishment(@PathVariable id: Long) {
        establishmentService.delete(id)
    }
}
