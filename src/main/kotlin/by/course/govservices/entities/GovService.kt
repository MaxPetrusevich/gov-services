package by.course.govservices.entities

import jakarta.persistence.*

@Entity
@Table(name = "service")
data class GovService(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String? = null,

    @Column(name = "description")
    val description: String? = null,

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    val category: Category? = null,

    @ManyToOne
    @JoinColumn(name = "establishment_id", referencedColumnName = "id", nullable = false)
    val establishment: Establishment? = null
)
