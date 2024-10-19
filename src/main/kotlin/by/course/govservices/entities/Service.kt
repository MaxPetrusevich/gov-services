package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "service")
data class Service(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name = "category", nullable = false)
    val category: String
) {
    constructor() : this(null, "", "", "")
}
