package by.course.govservices.config.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
class JwtTokenAuthenticationFilter(
    jwtTokenProvider: JwtTokenProviderImpl,
    securityContextRepository: ServerSecurityContextRepository
) : AuthenticationWebFilter(JwtReactiveAuthenticationManager(jwtTokenProvider)) {

    init {
        setSecurityContextRepository(securityContextRepository)
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return super.filter(exchange, chain)
    }
}
