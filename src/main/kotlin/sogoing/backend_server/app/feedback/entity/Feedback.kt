package sogoing.backend_server.app.feedback.entity

import com.uoslife.common.entity.BaseEntity
import jakarta.persistence.*
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.feedback.entity.enums.Busy
import sogoing.backend_server.app.feedback.entity.enums.Mood
import sogoing.backend_server.app.user.entity.User

@Entity
class Feedback(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int? = null,
    var description: String? = null,
    var rating: Int,
    @Enumerated(value = EnumType.STRING) var busy: Busy? = null,
    @Enumerated(value = EnumType.STRING) var mood: Mood? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    var department: Department,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") var user: User
) : BaseEntity()
