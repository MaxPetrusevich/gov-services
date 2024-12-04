package by.course.govservices.service

import by.course.govservices.dto.BidStatusDto
import by.course.govservices.entities.BidStatus
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.BidStatusRepository
import by.course.govservices.service.specifications.BidStatusSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class BidStatusService(
    private val bidStatusRepository: BidStatusRepository
) {

    fun findAll(page: Int, size: Int, filter: String?): List<BidStatusDto> {
        val pageRequest = PageRequest.of(page, size)

        // Формируем спецификацию на основе фильтра
        val specification = BidStatusSpecification(filter)

        return bidStatusRepository.findAll(specification, pageRequest)
            .map { it.toDto() }
            .toList()
    }

    fun findById(id: Long): BidStatusDto {
        val bidStatus = bidStatusRepository.findById(id).orElseThrow { NotFoundException("BidStatus with ID $id not found") }
        return bidStatus.toDto()
    }

    fun save(bidStatusDto: BidStatusDto): BidStatusDto {
        val bidStatus = bidStatusDto.toEntity()
        val savedBidStatus = bidStatusRepository.save(bidStatus)
        return savedBidStatus.toDto()
    }

    fun update(id: Long, bidStatusDto: BidStatusDto): BidStatusDto {
        if (!bidStatusRepository.existsById(id)) {
            throw NotFoundException("BidStatus with ID $id not found")
        }
        val bidStatus = bidStatusDto.toEntity()
        val updatedBidStatus = bidStatusRepository.save(bidStatus)
        return updatedBidStatus.toDto()
    }

    fun delete(id: Long) {
        if (!bidStatusRepository.existsById(id)) {
            throw NotFoundException("BidStatus with ID $id not found")
        }
        bidStatusRepository.deleteById(id)
    }
}
fun BidStatus.toDto(): BidStatusDto = BidStatusDto(id, status)
fun BidStatusDto.toEntity(): BidStatus = BidStatus(id, status)

