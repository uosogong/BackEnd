package sogoing.backend_server.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalArgumentException(
        ex: IllegalStateException
    ): ResponseEntity<Map<String, String>> {
        val body = mapOf("error" to ex.message.orEmpty())
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<Map<String, String>> {
        val body = mapOf("error" to "Internal Server Error", "message" to ex.message.orEmpty())
        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
