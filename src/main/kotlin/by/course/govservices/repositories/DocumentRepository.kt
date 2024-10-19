import by.course.govservices.entities.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface DocumentRepository : ReactiveMongoRepository<Document, String> {
    // Метод для получения всех документов по ID заявки (Bid)
    fun findByBidId(bidId: Long): Flux<Document>
}
