package by.course.govservices.service

import by.course.govservices.dto.BidDto
import by.course.govservices.entities.Bid
import by.course.govservices.entities.BidStatus
import by.course.govservices.entities.Citizen
import by.course.govservices.entities.GovService
import by.course.govservices.exceptions.BidNotFoundException
import by.course.govservices.repositories.BidRepository
import by.course.govservices.repositories.BidSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BidService(private val bidRepository: BidRepository) {

    // Получение заявки по ID
    fun findById(id: Long): BidDto {
        return bidRepository.findById(id)
            .map { it.toDto() }
            .orElseThrow { BidNotFoundException("Заявка с ID $id не найдена") }
    }

    // Получение всех заявок с фильтрацией
    fun findAll(page: Int, size: Int, date: String?, status: String?, serviceName: String?): Page<BidDto> {
        val pageRequest = PageRequest.of(page, size)
        val specification = BidSpecification(date, status, serviceName)
        return bidRepository.findAll(specification, pageRequest)
            .map { it.toDto() }
    }

    // Сохранение новой заявки
    fun save(bidDto: BidDto): BidDto {
        val bid = bidDto.toEntity()
        return bidRepository.save(bid).toDto()
    }

    // Обновление существующей заявки
    fun update(id: Long, bidDto: BidDto): BidDto {
        val existingBid = bidRepository.findById(id)
            .orElseThrow { BidNotFoundException("Заявка с ID $id не найдена") }

        val updatedBid = existingBid.copy(
            citizen = Citizen(bidDto.citizenId), // Используем только ID для обновления
            service = GovService(bidDto.serviceId),
            date = bidDto.date ?: existingBid.date,
            status = BidStatus(bidDto.statusId)
        )
        return bidRepository.save(updatedBid).toDto()
    }

    // Удаление заявки
    fun delete(id: Long) {
        val existingBid = bidRepository.findById(id)
            .orElseThrow { BidNotFoundException("Заявка с ID $id не найдена") }
        bidRepository.delete(existingBid)
    }
}
fun Bid.toDto(): BidDto = BidDto(
    id = this.id,
    citizenId = this.citizen?.id ?: throw IllegalStateException("Citizen ID must not be null"),
    serviceId = this.service?.id ?: throw IllegalStateException("Service ID must not be null"),
    date = this.date,
    statusId = this.status?.id ?: throw IllegalStateException("Status ID must not be null")
)

fun BidDto.toEntity(): Bid = Bid(
    id = this.id,
    citizen = Citizen(this.citizenId),
    service = GovService(this.serviceId),
    date = this.date ?: LocalDate.now(),
    status = BidStatus(this.statusId)
)
