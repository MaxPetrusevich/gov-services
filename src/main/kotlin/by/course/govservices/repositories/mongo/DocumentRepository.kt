package by.course.govservices.repositories.mongo
import by.course.govservices.entities.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
@Repository
interface DocumentRepository : ReactiveMongoRepository<Document, String> {
    fun findByBidId(bidId: Long): Flux<Document>
}
