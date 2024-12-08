package sogoing.backend_server.app.department.dto.request

import java.time.LocalDate
import sogoing.backend_server.app.department.entity.Department

class DepartmentUpdateRequestDto(
    val scholarshipRecruitment: Boolean,
    val internRecruitment: Boolean,
    val introduction: String?,
    val recruitEndDay: LocalDate?,
) {
    fun updateDepartment(department: Department) {
        department.scholarshipRecruitment = scholarshipRecruitment
        department.internRecruitment = internRecruitment
        department.introduction = introduction ?: department.introduction
        department.recruitEndDate = recruitEndDay ?: department.recruitEndDate
    }
}
