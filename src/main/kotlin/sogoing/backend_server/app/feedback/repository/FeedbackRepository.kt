package sogoing.backend_server.app.feedback.repository

import org.springframework.data.jpa.repository.JpaRepository
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.feedback.entity.Feedback
import sogoing.backend_server.app.user.entity.User

interface FeedbackRepository : JpaRepository<Feedback, Long> {
    fun findAllByDepartment(department: Department): List<Feedback>?

    fun findByDepartmentAndUser(department: Department, user: User): Feedback?

    fun findByUser(user: User): Feedback?
}
