package sogoing.backend_server.app.user.dto

import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.app.user.entity.UserRole

data class UserProfile(
    val id: Long?,
    val name: String?,
    val address: String?,
    val email: String?,
    val phone: String?,
    val role: UserRole?,
    val departmentName: String?,
    val schedule: String?,
    val studentId: String?,
) {
    companion object {
        fun from(user: User): UserProfile {
            return UserProfile(
                id = user.id,
                name = user.name,
                address = user.address,
                email = user.email,
                phone = user.phone,
                role = user.role,
                departmentName = user.department?.name,
                schedule = user.schedule,
                studentId = user.studentId
            )
        }
    }
}