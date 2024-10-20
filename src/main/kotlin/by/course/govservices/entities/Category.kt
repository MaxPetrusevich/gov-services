package by.course.govservices.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table(name = "category")
data class Category(

    @Id
    val id: Int? = null,
    @Column("category")
    val category: String
)