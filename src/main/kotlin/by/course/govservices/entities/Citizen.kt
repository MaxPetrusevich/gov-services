package by.course.govservices.entities

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "citizen")
data class Citizen(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(name = "date_of_birth", nullable = false)
    val dateOfBirth: LocalDate,

    @Column(name = "address", nullable = false)
    val address: String
) {
    constructor() : this(null, "", "", LocalDate.now(), "")
}
