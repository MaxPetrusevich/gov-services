package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "app_user")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "identify_number", nullable = false)
    val identifyNumber: String? = null,

    @Column(name = "password", nullable = false)
    val password: String? = null,

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    val role: Role? = null // Связь с сущностью Role
)
