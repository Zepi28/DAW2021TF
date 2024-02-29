package com.daw.controllers

import com.daw.API_PATH
import com.daw.repository.JDBI.ProjectJDBIRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API_PATH)
class APIController(private val service: ProjectJDBIRepository) {

    @GetMapping
    fun requestAPI(): ResponseEntity.BodyBuilder {
        println("API check request was made")
        return ResponseEntity.ok()
    }
}
