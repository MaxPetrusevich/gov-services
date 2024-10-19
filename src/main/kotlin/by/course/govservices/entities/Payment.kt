package by.course.govservices.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "payment")
data class Payment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "bid_id", nullable = false)
    val bidId: Int,

    @Column(name = "sum", nullable = false)
    val sum: BigDecimal,

    @Column(name = "date", nullable = false)
    val date: LocalDate = LocalDate.now(),

    @Column(name = "status_id", nullable = false)
    val statusId: Int
) {
    constructor() : this(null, 0, BigDecimal.ZERO, LocalDate.now(), 0)
}
