package by.course.govservices.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table(name = "role")
data class Role(

    @Id
    val id: Int? = null,

    @Column("role")
    val role: String
)
