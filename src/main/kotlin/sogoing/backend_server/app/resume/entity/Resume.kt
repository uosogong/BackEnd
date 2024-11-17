import com.uoslife.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "resume")
class Resume(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    var department: Department,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    var content: String? = null
) : BaseEntity()
