package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "category")
data class Category(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "category", nullable = false)
    val category: String
) {
    constructor() : this(null, "")
}
