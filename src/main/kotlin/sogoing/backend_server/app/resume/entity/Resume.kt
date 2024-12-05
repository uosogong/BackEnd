package sogoing.backend_server.app.resume.entity

import com.uoslife.common.entity.BaseEntity
import jakarta.persistence.*
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.resume.dto.ResumeCreateRequest
import sogoing.backend_server.app.user.entity.User

@Entity
@Table(name = "resumes")
class Resume(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @ManyToOne @JoinColumn(name = "department_id", nullable = false) var department: Department,
    @ManyToOne @JoinColumn(name = "user_id", nullable = false) var user: User,
    var isScholarshipResume: Boolean,
    var isInternResume: Boolean,
    var phone: String,
    var address: String,
    var workplace: String,
    var totalWorkSemester: String,
    var otherScholarship: String,
    var email: String,
    var schedule: String,
    var content: String,
) : BaseEntity() {
    companion object {
        fun from(request: ResumeCreateRequest, department: Department, user: User) =
            Resume(
                department = department,
                user = user,
                isScholarshipResume = request.isScholarshipResume,
                isInternResume = request.isInternResume,
                phone = request.phone,
                address = request.address,
                workplace = request.workplace,
                totalWorkSemester = request.totalWorkSemester,
                otherScholarship = request.otherScholarship,
                email = request.email,
                schedule = request.schedule,
                content = request.content,
            )
    }
}
