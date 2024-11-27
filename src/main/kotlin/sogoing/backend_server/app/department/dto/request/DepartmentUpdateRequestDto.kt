package sogoing.backend_server.app.department.dto.request

import java.time.LocalDate
import sogoing.backend_server.app.department.entity.Department

class DepartmentUpdateRequestDto(
    val scholarshipRecruitment: Boolean,
    val internRecruitment: Boolean,
    val name: String?,
    val introduction: String?,
    val recruitEndDay: LocalDate?,
) {
    fun updateDepartment(department: Department) {
        department.scholarshipRecruitment = scholarshipRecruitment
        department.internRecruitment = internRecruitment
        department.name = name
        department.introduction = introduction
        department.recruitEndDate = recruitEndDay
    }
}
