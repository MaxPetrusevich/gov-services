package by.course.govservices.repositories

import by.course.govservices.entities.Bid
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface BidRepository : JpaRepository<Bid, Long>, JpaSpecificationExecutor<Bid> {

    fun findByCitizenId(citizenId: Long): List<Bid>
}
