package by.course.govservices.repositories.postgres
import by.course.govservices.entities.Bid
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
@Repository
interface BidRepository : ReactiveCrudRepository<Bid, Long> {
    fun findByCitizenId(citizenId: Long): Flux<Bid>
}
