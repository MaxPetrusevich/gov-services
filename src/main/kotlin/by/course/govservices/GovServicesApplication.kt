package by.course.govservices

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@EnableR2dbcRepositories(basePackages = ["by.course.govservices.repositories"])
@SpringBootApplication(scanBasePackages = ["by.course.govservices"])
class GovServicesApplication

fun main(args: Array<String>) {
    runApplication<GovServicesApplication>(*args)
}
