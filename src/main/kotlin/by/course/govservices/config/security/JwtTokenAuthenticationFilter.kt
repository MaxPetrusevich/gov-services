package by.course.govservices.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.GenericFilterBean


class JwtTokenAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProviderImpl,
    private val authenticationManager: AuthenticationManager,
    private val requestMatcher: RequestMatcher
) : GenericFilterBean() {

    @Throws(ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        if (requestMatcher.matches(httpRequest)) {
            val token = extractTokenFromRequest(httpRequest)

            if (token != null && jwtTokenProvider.validateToken(token)) {
                val username = jwtTokenProvider.getUsernameFromToken(token)
                val roles = jwtTokenProvider.getRolesFromToken(token)
                val authorities = roles.map { SimpleGrantedAuthority(it) }
                val authentication = UsernamePasswordAuthenticationToken(username, null, authorities)

                authenticationManager.authenticate(authentication)
                    .apply {
                        // Set authentication details in the security context
                        SecurityContextHolder.getContext().authentication = authentication
                    }
            } else {
                httpResponse.status = HttpStatus.FORBIDDEN.value()
                return
            }
        }

        chain.doFilter(request, response)
    }

    private fun extractTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
}
