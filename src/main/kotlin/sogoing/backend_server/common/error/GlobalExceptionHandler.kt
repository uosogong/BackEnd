package sogoing.backend_server.common.error

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import sogoing.backend_server.common.error.exception.AccessDeniedException
import sogoing.backend_server.common.error.exception.EntityNotFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    // Specific handler for entity not found
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(
        exception: EntityNotFoundException
    ): ResponseEntity<ApiResponse> {
        logger.warn("Entity not found: ${exception.message}")
        return createErrorResponse(exception.errorCode)
    }

    // Specific handler for access denied
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(exception: AccessDeniedException): ResponseEntity<ApiResponse> {
        logger.warn("Access denied: ${exception.message}")
        return createErrorResponse(exception.errorCode)
    }

    // JWT-related handlers
    @ExceptionHandler(SignatureException::class)
    fun handleSignatureException(exception: SignatureException): ResponseEntity<ApiResponse> {
        logger.warn("Invalid signature: ${exception.message}")
        return createErrorResponse(ErrorCode.INVALID_SIGNATURE)
    }

    @ExceptionHandler(MalformedJwtException::class)
    fun handleMalformedJwtException(exception: MalformedJwtException): ResponseEntity<ApiResponse> {
        logger.warn("Malformed JWT token: ${exception.message}")
        return createErrorResponse(ErrorCode.MALFORMED_TOKEN)
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(exception: ExpiredJwtException): ResponseEntity<ApiResponse> {
        logger.warn("Expired JWT token: ${exception.message}")
        return createErrorResponse(ErrorCode.EXPIRED_TOKEN)
    }

    // Catch-all for uncaught exceptions
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ApiResponse> {
        logger.error("Unhandled exception: ${exception} ${exception.message}", exception)
        val errorCode = ErrorCode.fromException(exception)
        return createErrorResponse(errorCode)
    }

    // Helper method to create consistent error responses
    private fun createErrorResponse(errorCode: ErrorCode): ResponseEntity<ApiResponse> {
        return ResponseEntity.status(errorCode.status).body(errorCode.toApiResponse())
    }
}
