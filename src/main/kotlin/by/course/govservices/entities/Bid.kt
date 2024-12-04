package by.course.govservices.entities

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "bid")
data class Bid(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_id", referencedColumnName = "id")
    val citizen: Citizen? = null,  // Связь с сущностью Citizen

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    val service: GovService? = null,  // Связь с сущностью GovService

    @Column(name = "date")
    val date: LocalDate = LocalDate.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    val status: BidStatus? = null  // Связь с сущностью PaymentStatus
)
