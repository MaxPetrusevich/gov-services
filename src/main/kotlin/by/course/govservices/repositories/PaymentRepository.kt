package by.course.govservices.repositories

import by.course.govservices.entities.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

@Repository
interface PaymentRepository : JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    // Динамическое выполнение запроса с условиями
    @Query("SELECT p FROM Payment p WHERE p.date = :date")
    fun findByDate(@Param("date") date: LocalDate): List<Payment>
}
