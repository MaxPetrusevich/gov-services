package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "bid_status")
data class BidStatus(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "status", nullable = false)
    val status: String
) {
    constructor() : this(null, "")
}
