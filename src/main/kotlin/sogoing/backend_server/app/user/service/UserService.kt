package sogoing.backend_server.app.user.service

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sogoing.backend_server.app.department.service.DepartmentService
import sogoing.backend_server.app.user.dto.UserProfile
import sogoing.backend_server.app.user.dto.UserUpdateRequest
import sogoing.backend_server.app.user.repository.UserRepository

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val departmentService: DepartmentService
) {
    fun getProfile(userId: Long): UserProfile {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotFoundException()
        return UserProfile.from(user)
    }

    fun updateProfile(userId: Long, updateRequest: UserUpdateRequest): UserProfile {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotFoundException()

        user.apply {
            name = updateRequest.name ?: name
            address = updateRequest.address ?: address
            email = updateRequest.email ?: email
            phone = updateRequest.phone ?: phone
            studentId = updateRequest.studentId ?: studentId
            department =
                updateRequest.departmentName?.let { departmentService.getDepartmentByName(it) }
                    ?: department
            schedule = updateRequest.schedule ?: schedule
        }

        userRepository.save(user)

        return UserProfile.from(user)
    }
}
