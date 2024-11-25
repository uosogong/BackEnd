package sogoing.backend_server.app.dib.entity

import com.uoslife.common.entity.BaseEntity
import jakarta.persistence.*
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.user.entity.User

@Entity
@Table(name = "dibs")
class Dib(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @ManyToOne @JoinColumn(name = "user_id", nullable = false) var user: User,
    @ManyToOne @JoinColumn(name = "department_id", nullable = false) var department: Department
) : BaseEntity()
