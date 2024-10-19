package by.course.govservices.repositories

import by.course.govservices.entities.BidStatus
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface BidStatusRepository : ReactiveCrudRepository<BidStatus, Int>
