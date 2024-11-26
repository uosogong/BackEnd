package sogoing.backend_server.common.error.exception

import sogoing.backend_server.common.error.ErrorCode

open class AccessDeniedException : RuntimeException {

    val errorCode: ErrorCode

    constructor(message: String, errorCode: ErrorCode) : super(message) {
        this.errorCode = errorCode
    }

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }
}
