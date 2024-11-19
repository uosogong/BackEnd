package sogoing.backend_server.app.user.entity

import com.uoslife.common.entity.SoftDeleteEntity
import jakarta.persistence.*
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.feedback.entity.Feedback
import sogoing.backend_server.app.resume.entity.Resume

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @ManyToOne @JoinColumn(name = "department_id", nullable = false) var department: Department?=null,
    var phoneNumber: String? = null,
    var address: String? = null,
    var role: String? = null,
    var workplace: String? = null,
    var name: String? = null,
    var schedule: String? = null,
    var studentId: String? = null,
    var email: String? = null,
    var password: String? = null,
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var resumes: List<Resume> = mutableListOf(),
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var feedbacks: List<Feedback> = mutableListOf(),
) : SoftDeleteEntity()
