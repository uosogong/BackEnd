package sogoing.backend_server.app.dib.entity

import jakarta.persistence.*
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.common.entity.BaseEntity

@Entity
@Table(name = "dibs")
class Dib(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @ManyToOne @JoinColumn(name = "user_id", nullable = false) var user: User,
    @ManyToOne @JoinColumn(name = "department_id", nullable = false) var department: Department
) : BaseEntity() {
    companion object {
        fun create(user: User, department: Department): Dib {
            return Dib(user = user, department = department)
        }
    }
}
