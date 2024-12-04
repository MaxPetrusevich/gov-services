package by.course.govservices.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProviderImpl,
    private val jwtAuthenticationManager: JwtAuthenticationManager
) {

    // Определяем AuthenticationManager
    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .authenticationProvider(jwtAuthenticationManager)  // Используем JwtAuthenticationManager как AuthenticationProvider
            .build()
    }

    // Конфигурация HttpSecurity
    @Throws(Exception::class)
    fun configure(http: HttpSecurity) {
        http.csrf { csrf -> csrf.disable() } // Отключаем CSRF для работы с JWT
            .authorizeRequests() // Заменили на authorizeHttpRequests
            .requestMatchers(AntPathRequestMatcher("/api/auth/**")).permitAll()              .anyRequest().authenticated()  // Все остальные запросы требуют аутентификации
            .and()
            .addFilterBefore(
                JwtTokenAuthenticationFilter(jwtTokenProvider, authenticationManager(http), AntPathRequestMatcher("/api/**")),
                UsernamePasswordAuthenticationFilter::class.java
            )
    }
}
