package by.course.govservices.config.security

import by.course.govservices.entities.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.security.Key
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

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

    override fun validateToken(token: String): Mono<Boolean> {
        return try {
            val claims = getClaimsFromToken(token)
            val isTokenExpired = claims.expiration.before(Date())
            Mono.just(!isTokenExpired)
        } catch (e: Exception) {
            Mono.just(false)
        }
    }

    override fun getUsernameFromToken(token: String): Mono<String> {
        return try {
            val claims = getClaimsFromToken(token)
            Mono.just(claims.subject)
        } catch (e: Exception) {
            Mono.empty()
        }
    }

    override fun getRolesFromToken(token: String): Mono<List<String>> {
        return try {
            val claims = getClaimsFromToken(token)
            val roles = claims["roles"] as List<String>
            Mono.just(roles)
        } catch (e: Exception) {
            Mono.empty()
        }
    }

    private fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(token)
            .body
    }
}