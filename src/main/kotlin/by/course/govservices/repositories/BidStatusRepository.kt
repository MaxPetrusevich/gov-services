package by.course.govservices.repositories

import by.course.govservices.entities.BidStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface BidStatusRepository : JpaRepository<BidStatus, Long>, JpaSpecificationExecutor<BidStatus>
