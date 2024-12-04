package by.course.govservices.entities

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "document")
data class Document(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "link", nullable = false)
    val link: String? = null,

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    val type: Type? = null, // Связь с сущностью Type

    @Column(name = "loading_date")
    val loadingDate: LocalDate? = null,

    @ManyToOne
    @JoinColumn(name = "bid_id", referencedColumnName = "id")
    val bid: Bid? = null // Связь с сущностью Bid
)
