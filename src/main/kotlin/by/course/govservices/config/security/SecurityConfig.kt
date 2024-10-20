package by.course.govservices.config.security

import by.course.govservices.service.UserService // Make sure this is imported
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.web.reactive.config.WebFluxConfigurer
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val jwtTokenProvider: IJwtTokenProvider,
    private val userService: UserService // Injecting UserService
) {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { csrf -> csrf.disable() }
            .authorizeExchange { authz ->
                authz
                    .pathMatchers("/api/auth/**").permitAll()
                    .anyExchange().authenticated()
            }
            .authenticationManager(reactiveAuthenticationManager())
            .addFilterAt(jwtTokenAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }

    @Bean
    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager {
        return JwtReactiveAuthenticationManager(jwtTokenProvider)
    }

    @Bean
    fun jwtTokenAuthenticationFilter(): AuthenticationWebFilter {
        val authenticationWebFilter = AuthenticationWebFilter(reactiveAuthenticationManager())
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter())
        authenticationWebFilter.setRequiresAuthenticationMatcher(PathPatternParserServerWebExchangeMatcher("/api/**"))
        return authenticationWebFilter
    }

    private fun jwtAuthenticationConverter(): ServerAuthenticationConverter {
        return ServerAuthenticationConverter { exchange ->
            val token = extractTokenFromRequest(exchange.request)
            if (token != null) {
                jwtTokenProvider.validateToken(token)
                    .filter { it }
                    .flatMap {
                        val usernameMono = jwtTokenProvider.getUsernameFromToken(token)
                        val rolesMono = jwtTokenProvider.getRolesFromToken(token)
                        Mono.zip(usernameMono, rolesMono).map { tuple ->
                            UsernamePasswordAuthenticationToken(
                                tuple.t1, null, tuple.t2.map { SimpleGrantedAuthority(it) }
                            )
                        }
                    }
            } else {
                Mono.empty()
            }
        }
    }

    private fun extractTokenFromRequest(request: ServerHttpRequest): String? {
        val bearerToken = request.headers.getFirst("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
}
