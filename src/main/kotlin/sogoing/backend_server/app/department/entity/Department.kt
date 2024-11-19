package sogoing.backend_server.app.department.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import sogoing.backend_server.app.resume.entity.Resume
import sogoing.backend_server.common.entity.UpdateTimeEntity

@Entity
class Department(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    var departmentName: String? = null,
    var message: String? = null,
    var schedule: String? = null,
    var scholarshipRecruitment: Boolean? = null,
    var internRecruitment: Boolean? = null,
    var introduction: String? = null,
    @OneToMany(mappedBy = "department") val resumes: MutableList<Resume>? = mutableListOf()
) : UpdateTimeEntity()
