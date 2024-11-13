package by.course.govservices.repositories

import by.course.govservices.entities.Role
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@Repository
interface RoleRepository : ReactiveCrudRepository<Role, Long> {

    // Поиск по роли
    fun findByRole(role: String): Mono<Role>

    // Метод для получения всех ролей с фильтрацией
    fun findAllByRoleContainingIgnoreCase(role: String): Flux<Role>
}
