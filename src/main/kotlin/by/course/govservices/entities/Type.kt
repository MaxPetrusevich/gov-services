package by.course.govservices.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table(name = "type")
data class Type(

    @Id
    val id: Int? = null,

    @Column("type")
    val type: String
)
