package sogoing.backend_server.common.error

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import sogoing.backend_server.common.error.exception.AccessDeniedException
import sogoing.backend_server.common.error.exception.EntityNotFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ApiResponse> {
        println(exception.message)
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        return createErrorResponse(errorCode)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleRuntimeException(exception: EntityNotFoundException): ResponseEntity<ApiResponse> {
        val errorCode = exception.errorCode
        return createErrorResponse(errorCode)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(exception: AccessDeniedException): ResponseEntity<ApiResponse> {
        val errorCode = exception.errorCode
        return createErrorResponse(errorCode)
    }

    @ExceptionHandler(SignatureException::class)
    fun handleSignatureException(): ResponseEntity<ApiResponse> {
        return createErrorResponse(ErrorCode.INVALID_SIGNATURE)
    }

    @ExceptionHandler(MalformedJwtException::class)
    fun handleMalformedJwtException(): ResponseEntity<ApiResponse> {
        return createErrorResponse(ErrorCode.MALFORMED_TOKEN)
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(): ResponseEntity<ApiResponse> {
        return createErrorResponse(ErrorCode.EXPIRED_TOKEN)
    }

    // 공통 에러 응답 생성 메서드
    private fun createErrorResponse(errorCode: ErrorCode): ResponseEntity<ApiResponse> {
        val response = ApiResponse.error(errorCode)
        return ResponseEntity.status(HttpStatus.valueOf(errorCode.status)).body(response)
    }
}
