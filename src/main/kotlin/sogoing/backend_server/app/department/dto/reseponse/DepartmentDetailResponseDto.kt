package sogoing.backend_server.app.department.dto.reseponse

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.feedback.entity.Feedback

data class DepartmentDetailResponseDto(
    val departmentDetail: DepartmentDetail,
    val feedbacks: List<DepartmentFeedback>,
)

data class DepartmentDetail(
    val scholarshipRecruitment: Boolean,
    val internRecruitment: Boolean,
    val name: String?,
    val introduction: String?,
    val leftDays: Int?,
    val rating: Double,
    val updateAt: LocalDateTime?
) {
    companion object {
        fun convertToDto(department: Department): DepartmentDetail {
            return DepartmentDetail(
                scholarshipRecruitment = department.scholarshipRecruitment,
                internRecruitment = department.internRecruitment,
                name = department.name,
                introduction = department.introduction,
                leftDays =
                    department.recruitEndDate?.let {
                        ChronoUnit.DAYS.between(LocalDate.now(), it).toInt()
                    }
                        ?: -1,
                rating = 4.2,
                updateAt = department.updatedDate
            )
        }
    }
}

data class DepartmentFeedback(
    val name: String?,
    val busy: String?,
    val mood: String?,
    val description: String?,
    val rating: Int?,
    val update: LocalDateTime?,
) {
    companion object {
        fun convertToDto(feedbacks: List<Feedback>): List<DepartmentFeedback> {
            return feedbacks.map { feedback ->
                DepartmentFeedback(
                    name = feedback.user.name,
                    busy = feedback.busy,
                    mood = feedback.mood,
                    description = feedback.description,
                    rating = feedback.rating,
                    update = feedback.updatedDate
                )
            }
        }
    }
}
