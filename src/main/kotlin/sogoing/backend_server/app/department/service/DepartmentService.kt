package sogoing.backend_server.app.department.service

import java.lang.IllegalStateException
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sogoing.backend_server.app.auth.dto.SignUpRequest
import sogoing.backend_server.app.department.dto.request.DepartmentCreateRequestDto
import sogoing.backend_server.app.department.dto.request.DepartmentUpdateRequestDto
import sogoing.backend_server.app.department.dto.reseponse.*
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.department.repository.DepartmentRepository
import sogoing.backend_server.app.user.dto.UserProfile
import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.app.user.repository.UserRepository

@Service
@Transactional(readOnly = true)
class DepartmentService(
    private val encoder: PasswordEncoder,
    private val departmentRepository: DepartmentRepository,
    private val userRepository: UserRepository,
) {
    fun getDepartmentByName(name: String): Department {
        return departmentRepository.findByName(name) ?: throw ChangeSetPersister.NotFoundException()
    }

    fun getDepartmentsBasicInfo(): DepartmentBasicResponseDto {
        val departments =
            departmentRepository.findAllByOrderByUpdatedDateDesc()
                ?: return DepartmentBasicResponseDto(
                    listOf(),
                )

        val departmentBasicInfos: MutableList<DepartmentInfo> = mutableListOf()

        for (department in departments) {
            departmentBasicInfos.add(DepartmentInfo.convertToDto(department))
        }

        return DepartmentBasicResponseDto(
            departments = departmentBasicInfos,
        )
    }

    fun getDepartmentInfoWithFeedback(departmentId: Long): DepartmentDetailResponseDto {
        val department =
            departmentRepository.findByIdOrNull(departmentId)
                ?: throw IllegalStateException("부서 id 불일치")

        val feedbacks = department.feedbacks

        return DepartmentDetailResponseDto(
            departmentDetail = DepartmentDetail.convertToDto(department),
            feedbacks = DepartmentFeedback.convertToDto(feedbacks)
        )
    }

    @Transactional
    fun updateDepartmentStatus(
        userId: Long,
        departmentId: Long,
        departmentUpdateRequestDto: DepartmentUpdateRequestDto
    ): DepartmentUpdateResponseDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw IllegalStateException()
        val department =
            departmentRepository.findByIdOrNull(departmentId)
                ?: throw IllegalStateException("부서 id 불일치")

        if (department.user != user) {
            throw IllegalAccessError()
        }

        departmentUpdateRequestDto.updateDepartment(department)
        return DepartmentUpdateResponseDto.valueOf(department)
    }

    fun getTop5Department(): TopDepartmentResponseDto {
        val topDepartmentInfos: MutableList<TopDepartmentInfo> = mutableListOf()

        departmentRepository.findTop5ByOrderByDibsDesc()?.let { top5 ->
            for (department in top5) {
                topDepartmentInfos.add(TopDepartmentInfo.convertToDto(department))
            }
        }
        return TopDepartmentResponseDto(topDepartmentInfos)
    }

    @Transactional
    fun createDepartment(
        departmentCreateRequestDto: DepartmentCreateRequestDto
    ): List<UserProfile> {
        val departments: MutableList<Department> = mutableListOf()
        val adminUsers: MutableList<User> = mutableListOf()
        departmentCreateRequestDto.departments?.forEach { departmentDto ->
            val user =
                User.makeAdmin(
                    SignUpRequest(
                        name = departmentDto.name!!,
                        email = "${departmentDto.name}@uos.ac.kr",
                        password = departmentDto.name
                    ),
                    encoder = encoder
                )
            adminUsers.add(user)
            departments.add(
                Department(
                    name = departmentDto.name,
                    message = departmentDto.message,
                    introduction = departmentDto.introduction,
                    internRecruitment = false,
                    scholarshipRecruitment = false,
                    user = user,
                )
            )
        }
        val users = userRepository.saveAll(adminUsers)
        departmentRepository.saveAll(departments)

        return users.map { UserProfile.from(it) }
    }
}
