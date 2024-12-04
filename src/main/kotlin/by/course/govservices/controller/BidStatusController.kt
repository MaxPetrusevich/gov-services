package by.course.govservices.controller

import by.course.govservices.dto.BidStatusDto
import by.course.govservices.service.BidStatusService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bid-statuses")
class BidStatusController(private val bidStatusService: BidStatusService) {

    // Получение всех статусов заявок с пагинацией и фильтрацией
    @GetMapping
    fun getAllBidStatuses(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam filter: String? = null  // Новый параметр для фильтрации
    ): List<BidStatusDto> {
        return bidStatusService.findAll(page, size, filter)
    }

    // Получение статуса заявки по ID
    @GetMapping("/{id}")
    fun getBidStatusById(@PathVariable id: Long): BidStatusDto {
        return bidStatusService.findById(id)
    }

    // Создание нового статуса заявки
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBidStatus(@RequestBody bidStatusDto: BidStatusDto): BidStatusDto {
        return bidStatusService.save(bidStatusDto)
    }

    // Обновление существующего статуса заявки
    @PutMapping("/{id}")
    fun updateBidStatus(
        @PathVariable id: Long,
        @RequestBody bidStatusDto: BidStatusDto
    ): BidStatusDto {
        return bidStatusService.update(id, bidStatusDto)
    }

    // Удаление статуса заявки
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBidStatus(@PathVariable id: Long) {
        bidStatusService.delete(id)
    }
}
