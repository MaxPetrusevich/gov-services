package by.course.govservices.config.security

import by.course.govservices.entities.User
import reactor.core.publisher.Mono

interface IJwtTokenProvider {
    fun createToken(username: String, roles: List<String>): String
    fun validateToken(token: String): Mono<Boolean>
    fun getUsernameFromToken(token: String): Mono<String>
    fun getRolesFromToken(token: String): Mono<List<String>>
    fun generateToken(user: User): String

}