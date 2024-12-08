package sogoing.backend_server.app.user.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import org.springframework.security.crypto.password.PasswordEncoder
import sogoing.backend_server.app.auth.dto.SignUpRequest
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.feedback.entity.Feedback
import sogoing.backend_server.app.resume.entity.Resume
import sogoing.backend_server.common.entity.SoftDeleteEntity

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY) var department: Department? = null,
    var phone: String? = null,
    var address: String? = null,
    @Enumerated(EnumType.STRING) var role: UserRole? = null,
    var workplace: String? = null,
    var name: String? = null,
    var schedule: String? = null,
    var birthday: String? = null,
    var departmentName: String? = null,
    var studentId: String? = null,
    @Email var email: String? = null,
    var password: String? = null,
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var resumes: MutableList<Resume> = mutableListOf(),
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var feedbacks: MutableList<Feedback> = mutableListOf(),
) : SoftDeleteEntity() {
    companion object {
        fun from(request: SignUpRequest, encoder: PasswordEncoder) =
            User(
                name = request.name,
                email = request.email,
                password = encoder.encode(request.password),
                studentId = request.studentId,
                phone = request.phone,
                role = UserRole.USER
            )

        fun makeAdmin(request: SignUpRequest, encoder: PasswordEncoder) =
            User(
                name = request.name,
                email = request.email,
                password = encoder.encode(request.password),
                role = UserRole.ADMIN
            )
    }
}
