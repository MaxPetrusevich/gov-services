package by.course.govservices

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["by.course.govservices.repositories"])
@SpringBootApplication(scanBasePackages = ["by.course.govservices"])
class GovServicesApplication

fun main(args: Array<String>) {
    runApplication<GovServicesApplication>(*args)
}
