package by.course.govservices.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "service")
data class Service(

    @Id
    val id: Int? = null,

    @Column("name")
    val name: String,

    @Column("description")
    val description: String,

    @Column("category")
    val category: String
)
