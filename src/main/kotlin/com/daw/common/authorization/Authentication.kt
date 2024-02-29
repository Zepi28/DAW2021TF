package com.daw.common.authorization

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import org.springframework.util.DigestUtils
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Contract to be supported by credential's verification procedures
 */
typealias CredentialsVerifier = (credentials: String) -> UserInfo?

/**
 * Closed hierarchy (i.e. a sum type) used to support our simplistic RBAC (role-based access control) approach
 */
sealed class UserInfo(val name: String)
class Guest(name: String) : UserInfo(name)
class Owner(name: String) : UserInfo(name)

const val USER_ATTRIBUTE_KEY = "user-attribute"
const val BASIC_SCHEME = "Basic"

fun verifyBasicSchemeCredentials(challenge : String): UserInfo? {

    val pretenseUserDB = mapOf(
        "a" to Pair(Owner("a"), DigestUtils.md5Digest("a".encodeToByteArray())),
        "Fouto" to Pair(Owner("Fouto"), DigestUtils.md5Digest("isel".encodeToByteArray())),
        "Diogo" to Pair(Owner("Diogo"), DigestUtils.md5Digest("isel1".encodeToByteArray())),
        "Pedro" to Pair(Guest("Pedro"), DigestUtils.md5Digest("isel2".encodeToByteArray())),
        "TestOwner" to Pair(Owner("TestOwner"), DigestUtils.md5Digest("TestOwner".encodeToByteArray())),
        "TestGuest" to Pair(Guest("TestGuest"), DigestUtils.md5Digest("TestGuest".encodeToByteArray()))
    )

    // Note: This is not a real implementation of a user credential's verification procedure
    fun verifyUserCredentials(userId: String, pwd: String): UserInfo? {
        val credentials = pretenseUserDB[userId]
        return when {
            credentials == null -> null
            credentials.second.contentEquals(DigestUtils.md5Digest(pwd.encodeToByteArray())) -> credentials.first
            else -> null
        }
    }

    val trimmedChallengeResponse = challenge.trim()
    return if (trimmedChallengeResponse.startsWith(BASIC_SCHEME, ignoreCase = true)) {
        val userCredentials = trimmedChallengeResponse.drop(BASIC_SCHEME.length + 1).trim()
        val (userId, pwd) = String(Base64Utils.decodeFromString(userCredentials)).split(':')
        verifyUserCredentials(userId, pwd)
    }
    else {
        null
    }
}

/**
 * This is a sample filter that performs request logging.
 */
@Component
class AuthenticationFilter(private val credentialsVerifier: CredentialsVerifier) : Filter {

    private val logger = LoggerFactory.getLogger(AuthenticationFilter::class.java)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val httpRequest = request as HttpServletRequest
        val authorizationHeader: String = httpRequest.getHeader("authorization") ?: ""

        val userInfo = credentialsVerifier(authorizationHeader)
        if (userInfo != null) {
            logger.info("User credentials are valid. Proceeding.")
            httpRequest.setAttribute(USER_ATTRIBUTE_KEY, userInfo)

            chain?.doFilter(request, response)
        }
        else {
            logger.info("User credentials are invalid or were not provided. Issuing challenge.")
            val httpResponse = response as HttpServletResponse
            httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED
            httpResponse.addHeader(HttpHeaders.WWW_AUTHENTICATE, "$BASIC_SCHEME realm=\"daw\"")

        }
    }
}