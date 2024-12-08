package sogoing.backend_server.common.error

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException

enum class ErrorCode(val code: String, val message: String, val status: Int) {

    // Global Errors
    INVALID_INPUT_VALUE("C01", "Invalid Input Value.", HttpStatus.BAD_REQUEST.value()),
    METHOD_NOT_ALLOWED("C02", "Invalid Method Type.", HttpStatus.METHOD_NOT_ALLOWED.value()),
    ENTITY_NOT_FOUND("C03", "Entity Not Found.", HttpStatus.NOT_FOUND.value()),
    INTERNAL_SERVER_ERROR(
        "C04",
        "Internal Server Error.",
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    ),
    ACCESS_DENIED("C05", "Access is denied.", HttpStatus.UNAUTHORIZED.value()),

    // User-related Errors
    USER_NOT_FOUND("U01", "User not found.", HttpStatus.NOT_FOUND.value()),
    USER_EMAIL_ALREADY_EXISTING("U02", "User email already exists.", HttpStatus.CONFLICT.value()),

    // JWT and Token Errors
    INVALID_SIGNATURE("T01", "Invalid token signature.", HttpStatus.UNAUTHORIZED.value()),
    MALFORMED_TOKEN("T02", "Malformed token.", HttpStatus.UNAUTHORIZED.value()),
    EXPIRED_TOKEN("T03", "Token has expired.", HttpStatus.UNAUTHORIZED.value()),

    // Department Errors
    DEPARTMENT_NOT_FOUND("D01", "Department not found.", HttpStatus.NOT_FOUND.value()),

    // Feedback Errors
    FEEDBACK_NOT_FOUND("F01", "Feedback not found.", HttpStatus.NOT_FOUND.value());

    // Converts to ApiResponse
    fun toApiResponse(): ApiResponse = ApiResponse.error(this)

    companion object {
        // Maps general exceptions to appropriate error codes
        fun fromException(exception: Exception): ErrorCode {
            return when (exception) {
                is IllegalArgumentException -> INVALID_INPUT_VALUE
                is IllegalStateException -> METHOD_NOT_ALLOWED
                is AuthenticationException -> ACCESS_DENIED
                else -> INTERNAL_SERVER_ERROR
            }
        }
    }
}
