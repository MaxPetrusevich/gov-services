package by.course.govservices.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "citizen")
data class Citizen(

    @Id
    val id: Int? = null,

    @Column("first_name")
    val firstName: String,

    @Column("last_name")
    val lastName: String,

    @Column("middle_name")
    val middleName: String? = null, // Поле может быть null

    @Column("phone")
    val phone: String? = null, // Поле может быть null

    @Column("email")
    val email: String? = null, // Поле может быть null

    @Column("identify_number")
    val identifyNumber: String, // Обязательно к заполнению

    @Column("passport_series")
    val passportSeries: String? = null, // Поле может быть null

    @Column("passport_number")
    val passportNumber: String? = null, // Поле может быть null

    @Column("address")
    val address: String, // Обязательно к заполнению

    @Column("user_id")
    val userId: Int

)
