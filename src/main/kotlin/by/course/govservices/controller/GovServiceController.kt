package by.course.govservices.controller

import by.course.govservices.dto.ServiceDto
import by.course.govservices.service.GovServiceService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/govservices")
class GovServiceController(private val govServiceService: GovServiceService) {

    // Получение всех государственных услуг с фильтрами и пагинацией
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllGovServices(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam(required = false) name: String? = null,
        @RequestParam(required = false) description: String? = null,
        @RequestParam(required = false) categoryId: Long? = null
    ): List<ServiceDto> {
        val filters = mutableListOf<FilterCriteria>()
        name?.let { filters.add(FilterCriteria("name", it)) }
        description?.let { filters.add(FilterCriteria("description", it)) }
        categoryId?.let { filters.add(FilterCriteria("categoryId", it.toString())) }

        val pagination = PaginationRequest(page, size)
        return govServiceService.findAll(pagination, filters).content  // Возвращаем только содержимое страницы
    }

    // Получение услуги по названию
    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    fun getGovServiceByName(@PathVariable name: String): List<ServiceDto> {
        return govServiceService.findByName(name)
    }

    // Получение услуги по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getGovServiceById(@PathVariable id: Long): ServiceDto {
        return govServiceService.findById(id)
    }

    // Создание новой государственной услуги
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createGovService(@RequestBody serviceDto: ServiceDto): ServiceDto {
        return govServiceService.save(serviceDto)
    }

    // Обновление существующей государственной услуги
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateGovService(@PathVariable id: Long, @RequestBody serviceDto: ServiceDto): ServiceDto {
        return govServiceService.update(id, serviceDto)
    }

    // Удаление государственной услуги
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteGovService(@PathVariable id: Long) {
        govServiceService.delete(id)
    }
}
