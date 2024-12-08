package sogoing.backend_server.app.user.service

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sogoing.backend_server.app.department.service.DepartmentService
import sogoing.backend_server.app.user.dto.UserGetResponse
import sogoing.backend_server.app.user.dto.UserUpdateRequest
import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.app.user.repository.UserRepository

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val departmentService: DepartmentService,
    private val encoder: BCryptPasswordEncoder,
) {
    fun getUserById(userId: Long): User {
        return userRepository.findByIdOrNull(userId) ?: throw NotFoundException()
    }

    fun getProfile(userId: Long): UserGetResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotFoundException()
        return UserGetResponse.from(user)
    }

    fun updateProfile(userId: Long, updateRequest: UserUpdateRequest): UserGetResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotFoundException()

        user.apply {
            name = updateRequest.name ?: name
            address = updateRequest.address ?: address
            email = updateRequest.email ?: email
            studentId = updateRequest.studentId ?: studentId
            birthday = updateRequest.birthday ?: birthday
            department =
                updateRequest.departmentName?.let { departmentService.getDepartmentByName(it) }
                    ?: department
            schedule = updateRequest.schedule ?: schedule
            password = encoder.encode(updateRequest.password) ?: password
        }

        userRepository.save(user)

        return UserGetResponse.from(user)
    }

    fun deleteUserById(userId: Long) {
        userRepository.deleteById(userId)
    }
}
