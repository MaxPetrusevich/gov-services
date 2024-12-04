package by.course.govservices.config.security

import by.course.govservices.entities.User

interface IJwtTokenProvider {
    fun createToken(username: String, roles: List<String>): String
    fun validateToken(token: String): Boolean
    fun getUsernameFromToken(token: String): String
    fun getRolesFromToken(token: String): List<String>
    fun generateToken(user: User): String

}