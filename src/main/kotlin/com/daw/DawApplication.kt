package com.daw

import com.daw.common.authorization.UserInfo
import com.daw.common.authorization.verifyBasicSchemeCredentials
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.*


@SpringBootApplication
class DawApplication {

    @Bean
    fun authenticationProvider(): Function1<String, UserInfo?> = ::verifyBasicSchemeCredentials

}

private val logger = LoggerFactory.getLogger(DawApplication::class.java)

/**
 * Authentication, doFilters, interceptors...
 */
fun main(args: Array<String>) {
    val context = runApplication<DawApplication>(*args)
    context.getBeansWithAnnotation(RestController::class.java).forEach { (key, value) ->
        logger.info("Found controller bean '{}' with type '{}'", key, value.javaClass.name)
    }
}