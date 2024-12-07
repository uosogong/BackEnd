package sogoing.backend_server.app.department.entity

import jakarta.persistence.*
import java.time.LocalDate
import sogoing.backend_server.app.dib.entity.Dib
import sogoing.backend_server.app.feedback.entity.Feedback
import sogoing.backend_server.app.resume.entity.Resume
import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.common.entity.BaseEntity

@Entity
@Table(name = "departments")
class Department(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column(unique = true) var name: String? = null,
    @Column(columnDefinition = "TEXT") var introduction: String? = null,
    var scholarshipRecruitment: Boolean,
    var internRecruitment: Boolean,
    var recruitEndDate: LocalDate? = null,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    var dibs: MutableList<Dib> = mutableListOf(),
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    var resumes: MutableList<Resume> = mutableListOf(),
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    var feedbacks: MutableList<Feedback> = mutableListOf()
) : BaseEntity()
