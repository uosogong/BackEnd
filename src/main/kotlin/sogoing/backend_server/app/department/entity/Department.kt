import com.uoslife.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "department")
class Department(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String? = null,

    var message: String? = null,

    var introduction: String? = null,

    var schedule: String? = null,

    var scholarshipRecruitment: Boolean? = null,

    var internRecruitment: Boolean? = null,

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    var users: List<User> = mutableListOf(),

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    var dibs: List<Dib> = mutableListOf(),

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    var resumes: List<Resume> = mutableListOf(),

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    var feedbacks: List<Feedback> = mutableListOf()
) : BaseEntity()
