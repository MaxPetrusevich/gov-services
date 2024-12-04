package by.course.govservices.controller

import by.course.govservices.dto.BidDto
import by.course.govservices.service.BidService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bids")
class BidController(private val bidService: BidService) {

    // Получение заявки по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getBidById(@PathVariable id: Long): BidDto {
        return bidService.findById(id)
    }

    // Получение всех заявок с фильтрацией
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllBids(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam date: String? = null,  // Фильтр по дате
        @RequestParam status: String? = null,  // Фильтр по статусу
        @RequestParam serviceName: String? = null  // Фильтр по услуге
    ): List<BidDto> {
        return bidService.findAll(page, size, date, status, serviceName).content
    }

    // Создание новой заявки
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBid(@RequestBody bidDto: BidDto): BidDto {
        return bidService.save(bidDto)
    }

    // Обновление существующей заявки
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateBid(@PathVariable id: Long, @RequestBody bidDto: BidDto): BidDto {
        return bidService.update(id, bidDto)
    }

    // Удаление заявки
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBid(@PathVariable id: Long) {
        bidService.delete(id)
    }
}
