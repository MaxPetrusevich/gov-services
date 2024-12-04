package by.course.govservices.repositories

import by.course.govservices.entities.Bid
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate

class BidSpecification(
    private val date: String?,
    private val status: String?,
    private val serviceName: String?
) : Specification<Bid> {
    override fun toPredicate(root: Root<Bid>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder): Predicate? {
        val predicates = mutableListOf<Predicate>()

        // Фильтр по дате
        date?.let {
            val parsedDate = LocalDate.parse(it)
            predicates.add(criteriaBuilder.equal(root.get<LocalDate>("date"), parsedDate))
        }

        // Фильтр по статусу
        status?.let {
            predicates.add(criteriaBuilder.like(root.get<String>("statusId"), "%$status%"))
        }

        // Фильтр по услуге
        serviceName?.let {
            predicates.add(criteriaBuilder.like(root.get<String>("serviceId"), "%$serviceName%"))
        }

        return if (predicates.isNotEmpty()) {
            criteriaBuilder.and(*predicates.toTypedArray())
        } else null
    }
}
