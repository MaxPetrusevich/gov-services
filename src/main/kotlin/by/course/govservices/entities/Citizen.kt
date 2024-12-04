package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "citizen")
data class Citizen(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "first_name")
    val firstName: String? = null,

    @Column(name = "last_name")
    val lastName: String? = null,

    @Column(name = "middle_name")
    val middleName: String? = null,

    @Column(name = "phone")
    val phone: String? = null,

    @Column(name = "email")
    val email: String? = null,

    @Column(name = "identify_number")
    val identifyNumber: String? = null,

    @Column(name = "passport_series")
    val passportSeries: String? = null,

    @Column(name = "passport_number")
    val passportNumber: String? = null,

    @Column(name = "address")
    val address: String? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    val user: User? = null
)
