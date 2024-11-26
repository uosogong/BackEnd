package sogoing.backend_server.app.user.dto

data class UserUpdateRequest(
    val name: String?,
    val address: String?,
    val email: String?,
    val phone: String?,
    val departmentName: String?,
    val schedule: String?,
    val studentId: String?,
)
