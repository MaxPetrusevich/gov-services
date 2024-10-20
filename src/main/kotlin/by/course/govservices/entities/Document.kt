package by.course.govservices.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "document")
data class Document(

    @Id
    val id: Int? = null,

    @Column("link")
    val link: String,

    @Column("type_id")
    val typeId: Int,

    @Column("loading_date")
    val loadingDate: LocalDate? = null,

    @Column("bid_id")
    val bidId: Int? = null
)
