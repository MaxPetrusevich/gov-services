package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "role")
data class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "role_name", nullable = false)
    val roleName: String? = null,
)
