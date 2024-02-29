package com.daw.common.exceptions

import com.daw.common.exceptions.model.ProblemJson
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.URI
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ObjectNotFoundException::class)
    fun handleNotFound(
        ex: ObjectNotFoundException,
        req: HttpServletRequest
    ) : ResponseEntity<Any> {

        logger.info("Handling ObjectNotFoundException")
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(ProblemJson.MEDIA_TYPE)
            .body(
                ProblemJson(
                    System.currentTimeMillis(),
                    HttpStatus.NOT_FOUND.value(),
                    "Object not found",
                    ex.message,
                    URI(req.requestURI)
                ) as Any
            )
    }

    @ExceptionHandler(DuplicatedKeyException::class)
    fun handleDuplicatedKey(
        ex: DuplicatedKeyException,
        req: HttpServletRequest
    ) : ResponseEntity<Any> {

        logger.info("Handling handleDuplicatedKey")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(ProblemJson.MEDIA_TYPE)
            .body(
                ProblemJson(
                    System.currentTimeMillis(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Duplicated key",
                    ex.message,
                    URI(req.requestURI)
                ) as Any
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleIllegalCharacter(
        ex: IllegalCharacterException,
        req: HttpServletRequest
    ) : ResponseEntity<Any> {

        logger.info("Handling IllegalCharacter")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(ProblemJson.MEDIA_TYPE)
            .body(
                ProblemJson(
                    System.currentTimeMillis(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Illegal Character",
                    ex.message,
                    URI(req.requestURI)
                ) as Any
            )
    }
}