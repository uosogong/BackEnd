package sogoing.backend_server.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(val code: String, val message: String, var status: Int) {

    // Global
    INVALID_INPUT_VALUE("C01", "Invalid Input Value.", HttpStatus.BAD_REQUEST.value()),
    METHOD_NOT_ALLOWED("C02", "Invalid Method Type.", HttpStatus.METHOD_NOT_ALLOWED.value()),
    ENTITY_NOT_FOUND("C03", "Entity Not Found.", HttpStatus.BAD_REQUEST.value()),
    INTERNAL_SERVER_ERROR(
        "C04",
        "Internal Server Error.",
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    ),

    // User
    USER_NOT_FOUND("U01", "User is not Found.", HttpStatus.BAD_REQUEST.value()),
    USER_EMAIL_ALREADY_EXISTING(
        "U02",
        "User Email is already Existing",
        HttpStatus.BAD_REQUEST.value()
    ),

    // Token
    INVALID_SIGNATURE("T01", "Invalid token signature.", HttpStatus.UNAUTHORIZED.value()),
    MALFORMED_TOKEN("T02", "Malformed token.", HttpStatus.UNAUTHORIZED.value()),
    EXPIRED_TOKEN("T03", "Token has expired.", HttpStatus.UNAUTHORIZED.value())
}
