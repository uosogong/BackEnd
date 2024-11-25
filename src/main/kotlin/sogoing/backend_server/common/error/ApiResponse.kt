package sogoing.backend_server.common.error


data class ApiResponse(
    val message: Any,
    val status: ApiStatus,
    val code: String?,
) {
    companion object {
        fun success(message: Any): ApiResponse {
            return ApiResponse(status = ApiStatus.SUCCESS, message = message, code = null)
        }

        fun error(errorCode: ErrorCode): ApiResponse {
            return ApiResponse(status = ApiStatus.ERROR, message = errorCode.message, code = errorCode.code)
        }
    }
}