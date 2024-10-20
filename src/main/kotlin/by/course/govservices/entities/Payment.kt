package by.course.govservices.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate

@Table(name = "payment")
data class Payment(

    @Id
    val id: Int? = null,

    @Column("bid_id")
    val bidId: Int,

    @Column("sum")
    val sum: BigDecimal,

    @Column("date")
    val date: LocalDate = LocalDate.now(),

    @Column("status_id")
    val statusId: Int
)
