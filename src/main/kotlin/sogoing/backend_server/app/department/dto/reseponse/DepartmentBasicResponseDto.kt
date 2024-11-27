package sogoing.backend_server.app.department.dto.reseponse

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import sogoing.backend_server.app.department.entity.Department

data class DepartmentBasicResponseDto(
    val departments: List<DepartmentInfo>,
)

data class DepartmentInfo(val departmentId: Long, val content: InfoContent) {
    companion object {
        fun convertToDto(department: Department): DepartmentInfo {
            return DepartmentInfo(
                department.id!!,
                InfoContent(
                    scholarshipRecruitment = department.scholarshipRecruitment,
                    internRecruitment = department.internRecruitment,
                    introduction = department.introduction,
                    leftDays =
                        department.recruitEndDate?.let {
                            ChronoUnit.DAYS.between(LocalDate.now(), it).toInt()
                        }
                            ?: -1,
                    name = department.name,
                    rating = 4.2,
                    updateAt = department.updatedDate
                )
            )
        }
    }
}

data class InfoContent(
    val scholarshipRecruitment: Boolean,
    val internRecruitment: Boolean,
    val name: String?,
    val introduction: String?,
    val leftDays: Int?,
    val rating: Double,
    val updateAt: LocalDateTime?
)
