package by.course.govservices.repositories.postgres

import by.course.govservices.entities.BidStatus
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BidStatusRepository : ReactiveCrudRepository<BidStatus, Int>
