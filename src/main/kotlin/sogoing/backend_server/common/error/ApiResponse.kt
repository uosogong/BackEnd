package sogoing.backend_server.common.error

data class ApiResponse(
    val status: ApiStatus,
    val code: String?,
    val message: Any,
    val value: Any?
) {
    companion object {
        fun success(message: Any): ApiResponse {
            return ApiResponse(
                status = ApiStatus.SUCCESS,
                message = message,
                code = null,
                value = null
            )
        }

        fun error(errorCode: ErrorCode): ApiResponse {
            return ApiResponse(
                status = ApiStatus.ERROR,
                value = errorCode.status,
                message = errorCode.message,
                code = errorCode.code,
            )
        }
    }
}
