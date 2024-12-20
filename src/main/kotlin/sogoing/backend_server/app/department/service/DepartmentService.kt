package sogoing.backend_server.app.department.service

import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sogoing.backend_server.app.auth.dto.SignUpRequest
import sogoing.backend_server.app.department.dao.DepartmentDao
import sogoing.backend_server.app.department.dto.request.DepartmentCreateRequestDto
import sogoing.backend_server.app.department.dto.request.DepartmentDto
import sogoing.backend_server.app.department.dto.request.DepartmentUpdateRequestDto
import sogoing.backend_server.app.department.dto.reseponse.*
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.department.repository.DepartmentRepository
import sogoing.backend_server.app.dib.service.DibService
import sogoing.backend_server.app.feedback.service.FeedbackService
import sogoing.backend_server.app.user.dto.UserGetResponse
import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.app.user.repository.UserRepository
import sogoing.backend_server.common.error.exception.department.DepartmentNotFoundException

@Service
@Transactional(readOnly = true)
class DepartmentService(
    private val encoder: PasswordEncoder,
    private val departmentRepository: DepartmentRepository,
    private val userRepository: UserRepository,
    private val departmentDao: DepartmentDao,
    private val feedbackService: FeedbackService,
    private val dibService: DibService,
) {
    fun getDepartmentByName(name: String): Department {
        return departmentRepository.findByName(name) ?: throw ChangeSetPersister.NotFoundException()
    }

    fun getDepartmentById(id: Long): Department {
        return departmentRepository.findByIdOrNull(id)
            ?: throw ChangeSetPersister.NotFoundException()
    }

    fun getDepartmentsBasicInfo(userId: Long): DepartmentBasicResponseDto {
        val departments =
            departmentRepository.findAllByOrderByUpdatedDateDesc()
                ?: return DepartmentBasicResponseDto(
                    listOf(),
                )

        val departmentBasicInfos: MutableList<DepartmentInfo> = mutableListOf()

        for (department in departments) {
            val calculateRating = calculateRating(department)
            val isUserDibs = if (userId == 0L) false else getUserDibs(department.id!!, userId)
            val departmentInfo =
                DepartmentInfo.convertToDto(department, calculateRating, isUserDibs)
            departmentBasicInfos.add(departmentInfo)
        }
        return DepartmentBasicResponseDto(
            departments = departmentBasicInfos,
        )
    }

    private fun getUserDibs(departmentId: Long, userId: Long): Boolean {
        return dibService.getDib(departmentId, userId).userDib
    }

    private fun calculateRating(department: Department): Float {
        val feedbacks = feedbackService.findDepartmentFeedbacks(department.id)
        feedbacks.feedbackList ?: return 0F
        var sum = 0F
        for (feedback in feedbacks.feedbackList) {
            sum += feedback.rating
        }
        return sum / feedbacks.feedbackList.size
    }

    fun getDepartmentInfoWithFeedback(departmentId: Long): DepartmentDetailResponseDto {
        val department =
            departmentRepository.findByIdOrNull(departmentId) ?: throw DepartmentNotFoundException()

        val rating = calculateRating(department)
        val feedbacks = department.feedbacks

        return DepartmentDetailResponseDto(
            departmentDetail = DepartmentDetail.convertToDto(department, rating),
            feedbacks = DepartmentFeedback.convertToDto(feedbacks)
        )
    }

    @Transactional
    fun updateDepartmentStatus(
        userId: Long,
        departmentId: Long,
        departmentUpdateRequestDto: DepartmentUpdateRequestDto,
    ): DepartmentUpdateResponseDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw IllegalStateException()
        val department =
            departmentRepository.findByIdOrNull(departmentId) ?: throw DepartmentNotFoundException()

        if (department.user != user) {
            throw IllegalAccessError()
        }

        departmentUpdateRequestDto.updateDepartment(department)
        return DepartmentUpdateResponseDto.valueOf(department)
    }

    fun getTop5Department(): TopDepartmentResponseDto {
        val topDepartmentInfos: MutableList<TopDepartmentInfo> = mutableListOf()

        departmentDao.findTop5DepartmentsByDibsCount()?.let { top5 ->
            for (department in top5) {
                topDepartmentInfos.add(TopDepartmentInfo.convertToDto(department))
            }
        }
        return TopDepartmentResponseDto(topDepartmentInfos)
    }

    @Transactional
    fun createDepartment(
        departmentCreateRequestDto: DepartmentCreateRequestDto,
    ): List<UserGetResponse> {
        val departments: MutableList<Department> = mutableListOf()
        val adminUsers: MutableList<User> = mutableListOf()
        departmentCreateRequestDto.departments?.forEach { departmentDto ->
            val user = makeAdminUser(departmentDto)
            adminUsers.add(user)
            departments.add(
                Department(
                    name = departmentDto.name,
                    introduction = departmentDto.introduction,
                    internRecruitment = false,
                    scholarshipRecruitment = false,
                    user = user,
                )
            )
        }
        val users = userRepository.saveAll(adminUsers)
        departmentRepository.saveAll(departments)

        return users.map { UserGetResponse.from(it) }
    }

    private fun makeAdminUser(departmentDto: DepartmentDto): User {
        return User.makeAdmin(
            SignUpRequest(
                name = departmentDto.name,
                email = "${departmentDto.name}@uos.ac.kr",
                password = departmentDto.name,
                studentId = "",
                phone = "",
            ),
            encoder = encoder
        )
    }
}
