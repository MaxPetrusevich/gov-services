package by.course.govservices.service.base

import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface BaseService<T, ID> {
    fun findAll(pagination: PaginationRequest, filters: List<FilterCriteria>): Flux<T>
    fun findById(id: ID): Mono<T>
    fun save(entity: T): Mono<T>
    fun update(id: ID, entity: T): Mono<T>
    fun delete(id: ID): Mono<Void>
}