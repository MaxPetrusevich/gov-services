package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "user")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "identify_number", nullable = false, unique = true)
    val identifyNumber: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "role_id", nullable = false)
    val roleId: Int
) {
    constructor() : this(null, "", "", 0)
}
