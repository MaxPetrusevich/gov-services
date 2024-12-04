    package by.course.govservices.repositories

    import by.course.govservices.entities.Citizen
    import org.springframework.data.jpa.repository.JpaRepository
    import org.springframework.data.jpa.repository.JpaSpecificationExecutor
    import org.springframework.stereotype.Repository
    import org.springframework.data.jpa.repository.Query
    import org.springframework.data.repository.query.Param

    @Repository
    interface CitizenRepository : JpaRepository<Citizen, Long>, JpaSpecificationExecutor<Citizen> {

        // Динамическое выполнение запроса с условиями
        @Query("SELECT c FROM Citizen c WHERE c.firstName LIKE %:name%")
        fun findByFirstNameContaining(@Param("name") name: String): List<Citizen>

        fun findByIdentifyNumber(identity: String): Citizen?
    }
