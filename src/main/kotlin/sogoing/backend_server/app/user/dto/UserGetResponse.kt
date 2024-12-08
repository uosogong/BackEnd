package sogoing.backend_server.app.user.dto

import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.app.user.entity.UserRole

data class UserGetResponse(
    val id: Long?,
    val name: String?,
    val address: String?,
    val email: String?,
    val phone: String?,
    val role: UserRole?,
    val departmentName: String?,
    val schedule: String?,
    val studentId: String?,
    val birthDay: String?,
) {
    companion object {
        fun from(user: User): UserGetResponse {
            return UserGetResponse(
                id = user.id,
                name = user.name,
                address = user.address,
                email = user.email,
                phone = user.phone,
                role = user.role,
                departmentName = user.departmentName,
                schedule = user.schedule,
                studentId = user.studentId,
                birthDay = user.birthday
            )
        }
    }
}
