package sogoing.backend_server.app.user.entity

import com.uoslife.common.entity.SoftDeleteEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import org.springframework.security.crypto.password.PasswordEncoder
import sogoing.backend_server.app.auth.dto.SignUpRequest
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.feedback.entity.Feedback
import sogoing.backend_server.app.resume.entity.Resume

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    var department: Department? = null,
    var phone: String? = null,
    var address: String? = null,
    @Enumerated(EnumType.STRING) var role: UserRole? = null,
    var workplace: String? = null,
    var name: String? = null,
    var schedule: String? = null,
    var studentId: String? = null,
    @Email var email: String? = null,
    var password: String? = null,
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var resumes: List<Resume> = mutableListOf(),
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var feedbacks: List<Feedback> = mutableListOf(),
) : SoftDeleteEntity() {
    companion object {
        fun from(request: SignUpRequest, encoder: PasswordEncoder) =
            User(
                name = request.name,
                email = request.email,
                password = encoder.encode(request.password),
            )
    }

    @PrePersist
    @PreUpdate
    fun updateRole() {
        role = if (department == null) UserRole.USER else UserRole.ADMIN
    }
}
