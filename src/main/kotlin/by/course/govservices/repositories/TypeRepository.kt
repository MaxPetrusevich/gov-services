package by.course.govservices.repositories

import by.course.govservices.entities.Type
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface TypeRepository : JpaRepository<Type, Long>, JpaSpecificationExecutor<Type> {

    fun findByType(code: String): Type?
}
