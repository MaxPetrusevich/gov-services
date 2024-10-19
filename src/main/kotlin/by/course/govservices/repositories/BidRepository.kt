import by.course.govservices.entities.Bid
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface BidRepository : ReactiveCrudRepository<Bid, Long> {
    // Метод для получения всех заявок по ID гражданина
    fun findByCitizenId(citizenId: Long): Flux<Bid>
}
