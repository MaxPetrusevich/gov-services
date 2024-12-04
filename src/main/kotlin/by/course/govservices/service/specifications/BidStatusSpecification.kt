package by.course.govservices.service.specifications
import by.course.govservices.entities.BidStatus
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class BidStatusSpecification(private val filter: String?) : Specification<BidStatus> {
    override fun toPredicate(
        root: Root<BidStatus>,
        query: CriteriaQuery<*>?,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        if (filter.isNullOrBlank()) return null

        // Фильтрация по категории (например, по названию статуса)
        return criteriaBuilder.like(root.get<String>("status"), "%$filter%")    }
}
