package by.course.govservices.repositories
import by.course.govservices.entities.PaymentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface PaymentStatusRepository : JpaRepository<PaymentStatus, Long>, JpaSpecificationExecutor<PaymentStatus>
