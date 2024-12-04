package by.course.govservices.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "payment")
data class Payment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "bid_id", referencedColumnName = "id")
    val bid: Bid? = null, // Связь с сущностью Bid

    @Column(name = "sum", nullable = false)
    val sum: BigDecimal? = null,

    @Column(name = "date", nullable = false)
    val date: LocalDate = LocalDate.now(),

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    val status: PaymentStatus? = null // Связь с сущностью PaymentStatus
)
