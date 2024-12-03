package sogoing.backend_server.app.resume.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sogoing.backend_server.app.department.service.DepartmentService
import sogoing.backend_server.app.resume.dto.ResumeCreateRequest
import sogoing.backend_server.app.resume.entity.Resume
import sogoing.backend_server.app.resume.repository.ResumeRepository
import sogoing.backend_server.app.user.service.UserService

@Service
@Transactional
class ResumeService(
    private val resumeRepository: ResumeRepository,
    private val departmentService: DepartmentService,
    private val userService: UserService,
) {

    fun createResume(userId: Long, departmentId: Long, request: ResumeCreateRequest) {
        val department = departmentService.getDepartmentById(departmentId)
        val user = userService.getUserById(userId)
        val resume = Resume.from(request, department, user)

        resumeRepository.save(resume)
    }
}