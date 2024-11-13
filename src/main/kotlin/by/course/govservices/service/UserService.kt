package by.course.govservices.service

import by.course.govservices.entities.User
import by.course.govservices.repositories.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userRepository: UserRepository // Inject your UserRepository
) : ReactiveUserDetailsService {

    override fun findByUsername(username: String): Mono<UserDetails> {
        return userRepository.findByIdentifyNumber(username)
            .map { user ->
                org.springframework.security.core.userdetails.User
                    .withUsername(user.identifyNumber)
                    .password(user.password)
                    .authorities("ROLE_USER") // Assign roles accordingly
                    .build()
            }
            .switchIfEmpty(Mono.error(UsernameNotFoundException("User not found")))
    }

    fun save(user: User): Mono<User> {
        return userRepository.save(user)
    }
}
