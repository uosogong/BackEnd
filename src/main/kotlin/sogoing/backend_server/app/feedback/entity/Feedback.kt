package sogoing.backend_server.app.feedback.entity

import com.uoslife.common.entity.BaseEntity
import jakarta.persistence.*
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.user.entity.User

@Entity
@Table(name = "feedbacks")
class Feedback(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    var department: Department,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    var description: String? = null,

    var rating: Int? = null,

    var busy: String? = null,

    var mood: String? = null
) : BaseEntity()