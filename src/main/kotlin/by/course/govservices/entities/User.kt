package by.course.govservices.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.stereotype.Component

@Table(name = "app_user")
data class User(

    @Id
    val id: Int? = null,

    @Column("identify_number")
    val identifyNumber: String,

    @Column("password")
    val password: String,

    @Column("role_id")
    val roleId: Int
)