package by.course.govservices.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table(name = "payment_status")
data class PaymentStatus(

    @Id
    val id: Int? = null,

    @Column("status")
    val status: String
)
