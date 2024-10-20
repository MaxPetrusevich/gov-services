// JwtReactiveAuthenticationManager.kt
package by.course.govservices.config.security

import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
@Primary
@Component
class JwtReactiveAuthenticationManager(
    private val jwtTokenProvider: IJwtTokenProvider
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val token = authentication.credentials as String
        return jwtTokenProvider.validateToken(token).flatMap { isValid ->
            if (isValid) {
                jwtTokenProvider.getUsernameFromToken(token).flatMap { username ->
                    jwtTokenProvider.getRolesFromToken(token).map { roles ->
                        val authorities: List<GrantedAuthority> = roles.map { SimpleGrantedAuthority(it) }
                        UsernamePasswordAuthenticationToken(username, token, authorities)
                    }
                }
            } else {
                Mono.empty()
            }
        }
    }
}
