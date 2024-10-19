package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "type")
data class Type(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "type", nullable = false)
    val type: String
) {
    constructor() : this(null, "")
}
