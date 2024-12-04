package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "payment_status")
data class PaymentStatus(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "status", nullable = false)
    val status: String ?= null
)
