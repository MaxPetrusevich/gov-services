package by.course.govservices.controller

import by.course.govservices.dto.CitizenDto
import by.course.govservices.service.CitizenService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/citizens")
class CitizenController(private val citizenService: CitizenService) {

    // Получение гражданина по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getCitizenById(@PathVariable id: Long): CitizenDto {
        return citizenService.findById(id)
    }

    // Получение всех граждан с фильтрами и пагинацией
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCitizens(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam(required = false) firstName: String? = null,
        @RequestParam(required = false) lastName: String? = null,
        @RequestParam(required = false) identifyNumber: String? = null,
        @RequestParam(required = false) address: String? = null,
        @RequestParam(required = false) email: String? = null
    ): List<CitizenDto> {
        val filters = mutableListOf<FilterCriteria>()
        firstName?.let { filters.add(FilterCriteria("firstName", it)) }
        lastName?.let { filters.add(FilterCriteria("lastName", it)) }
        identifyNumber?.let { filters.add(FilterCriteria("identifyNumber", it)) }
        address?.let { filters.add(FilterCriteria("address", it)) }
        email?.let { filters.add(FilterCriteria("email", it)) }

        val pagination = PaginationRequest(page, size)
        return citizenService.findAll(pagination, filters).content // Возвращаем content из Page
    }

    // Создание нового гражданина
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCitizen(@RequestBody citizenDto: CitizenDto): CitizenDto {
        return citizenService.save(citizenDto)
    }

    // Обновление существующего гражданина
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCitizen(@PathVariable id: Long, @RequestBody citizenDto: CitizenDto): CitizenDto {
        return citizenService.update(id, citizenDto)
    }

    // Удаление гражданина
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCitizen(@PathVariable id: Long) {
        citizenService.delete(id)
    }
}
