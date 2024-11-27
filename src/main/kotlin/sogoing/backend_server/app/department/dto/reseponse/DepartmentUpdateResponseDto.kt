package sogoing.backend_server.app.department.dto.reseponse

import java.time.LocalDateTime
import sogoing.backend_server.app.department.entity.Department

data class DepartmentUpdateResponseDto(
    val scholarshipRecruitment: Boolean,
    val internRecruitment: Boolean,
    val name: String?,
    val introduction: String?,
    val updateAt: LocalDateTime?
) {
    companion object {
        fun valueOf(department: Department): DepartmentUpdateResponseDto {
            return DepartmentUpdateResponseDto(
                scholarshipRecruitment = department.scholarshipRecruitment,
                internRecruitment = department.internRecruitment,
                name = department.name,
                introduction = department.introduction,
                updateAt = department.updatedDate
            )
        }
    }
}
