package by.course.govservices.entities

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "bid")
data class Bid(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "citizen_id", nullable = false)
    val citizenId: Int,

    @Column(name = "service_id", nullable = false)
    val serviceId: Int,

    @Column(name = "date", nullable = false)
    val date: LocalDate = LocalDate.now(),

    @Column(name = "status_id", nullable = false)
    val statusId: Int
) {
    // No-arg constructor required by JPA
    constructor() : this(null, 0, 0, LocalDate.now(), 0)
}
