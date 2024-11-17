import com.uoslife.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "dib")
class Dib(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    var department: Department
) : BaseEntity()
