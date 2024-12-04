package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "establishment")
data class Establishment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String? = null
)
