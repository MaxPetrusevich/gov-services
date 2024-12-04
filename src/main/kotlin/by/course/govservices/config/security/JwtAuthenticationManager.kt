package by.course.govservices.config.security

import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Primary
@Component
class JwtAuthenticationManager(
    private val jwtTokenProvider: IJwtTokenProvider
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication.credentials as String
        val isValid = jwtTokenProvider.validateToken(token)

        if (isValid) {
            val username = jwtTokenProvider.getUsernameFromToken(token)
            val roles = jwtTokenProvider.getRolesFromToken(token)
            val authorities: List<GrantedAuthority> = roles.map { SimpleGrantedAuthority(it) }
            return UsernamePasswordAuthenticationToken(username, token, authorities)
        } else {
            throw IllegalArgumentException("Invalid token")
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}
