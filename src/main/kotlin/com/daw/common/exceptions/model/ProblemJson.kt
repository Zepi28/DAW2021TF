package com.daw.common.exceptions.model

import org.springframework.http.MediaType
import java.net.URI

data class ProblemJson(
    val timestamp: Long,
    val status: Int,
    val error: String,
    val message: String?,
    val path: URI
) {
    companion object {
        val MEDIA_TYPE = MediaType.parseMediaType("application/problem+json")
    }
}