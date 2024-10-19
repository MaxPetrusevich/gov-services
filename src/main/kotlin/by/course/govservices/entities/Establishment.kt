package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "establishment")
data class Establishment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "name", nullable = false)
    val name: String
) {
    constructor() : this(null, "")
}
