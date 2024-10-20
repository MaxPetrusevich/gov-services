package by.course.govservices.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "bid")
data class Bid(
    @Id
    val id: Int? = null,
    @Column("citizen_id")
    val citizenId: Int,
    @Column("service_id")
    val serviceId: Int,
    @Column("date")
    val date: LocalDate = LocalDate.now(),
    @Column("status_id")
    val statusId: Int
)
