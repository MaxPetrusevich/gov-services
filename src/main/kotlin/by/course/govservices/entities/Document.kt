package by.course.govservices.entities

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "document")
data class Document(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "link", nullable = false)
    val link: String,

    @Column(name = "type_id", nullable = false)
    val typeId: Int,

    @Column(name = "loading_date")
    val loadingDate: LocalDate? = null,

    @Column(name = "bid_id")
    val bidId: Int? = null
) {
    constructor() : this(null, "", 0, null, null)
}
