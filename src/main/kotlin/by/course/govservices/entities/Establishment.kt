package by.course.govservices.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table(name = "establishment")
data class Establishment(

    @Id
    val id: Int? = null,

    @Column( "name")
    val name: String
)
