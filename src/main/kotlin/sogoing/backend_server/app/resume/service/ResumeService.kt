package sogoing.backend_server.app.resume.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sogoing.backend_server.app.department.service.DepartmentService
import sogoing.backend_server.app.resume.dao.ResumeDao
import sogoing.backend_server.app.resume.dto.ResumeCreateRequest
import sogoing.backend_server.app.resume.dto.ResumeGetResponse
import sogoing.backend_server.app.resume.entity.Resume
import sogoing.backend_server.app.resume.repository.ResumeRepository
import sogoing.backend_server.app.user.service.UserService

@Service
@Transactional
class ResumeService(
    private val resumeRepository: ResumeRepository,
    private val departmentService: DepartmentService,
    private val userService: UserService,
    private val resumeDao: ResumeDao,
) {

    fun createResume(userId: Long, departmentId: Long, request: ResumeCreateRequest) {
        val department = departmentService.getDepartmentById(departmentId)
        val user = userService.getUserById(userId)
        val resume = Resume.from(request, department, user)

        resumeRepository.save(resume)
    }

    fun findResume(userId: Long): List<ResumeGetResponse> {
        val department = userService.getUserById(userId).department ?: throw NotFoundException()

        val resumes =
            resumeDao.findByDepartmentIdAndResumeType(
                department.id!!,
                isInternResume = department.internRecruitment,
                isScholarShipResume = department.scholarshipRecruitment
            )
                ?: throw EntityNotFoundException()

        return resumes.stream().map(ResumeGetResponse::from).toList()
    }
}
