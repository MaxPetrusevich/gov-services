package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "role")
data class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "role", nullable = false)
    val role: String
) {
    constructor() : this(null, "")
}
