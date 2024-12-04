package by.course.govservices.config.security

import by.course.govservices.entities.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Suppress("UNCHECKED_CAST", "DEPRECATION")
@Component
class JwtTokenProviderImpl(
    @Value("\${auth.secretKey}")
    private val secretKey: String
) : IJwtTokenProvider {

    private val signingKey: Key = Keys.hmacShaKeyFor(secretKey.toByteArray(Charsets.UTF_8).copyOf(32))

    override fun generateToken(user: User): String {
        return Jwts.builder()
            .setSubject(user.identifyNumber)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 86400000)) // 1 день
            .signWith(signingKey)
            .compact()
    }

    override fun createToken(username: String, roles: List<String>): String {
        val claims = Jwts.claims().setSubject(username)
        claims["roles"] = roles
        val now = Date()
        val validity = Date(now.time + 3600000) // Токен валиден 1 час

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(signingKey)
            .compact()
    }

    override fun validateToken(token: String): Boolean {
        return try {
            val claims = getClaimsFromToken(token)
            val isTokenExpired = claims.expiration.before(Date())
            !isTokenExpired
        } catch (e: Exception) {
            false
        }
    }

    override fun getUsernameFromToken(token: String): String {
        return try {
            val claims = getClaimsFromToken(token)
            claims.subject
        } catch (e: Exception) {
            ""
        }
    }

    override fun getRolesFromToken(token: String): List<String> {
        return try {
            val claims = getClaimsFromToken(token)
            claims["roles"] as List<String>
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(token)
            .body
    }
}
