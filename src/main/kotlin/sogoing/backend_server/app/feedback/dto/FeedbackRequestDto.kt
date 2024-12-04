package sogoing.backend_server.app.feedback.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.feedback.entity.Feedback
import sogoing.backend_server.app.feedback.entity.enums.Busy
import sogoing.backend_server.app.feedback.entity.enums.Mood
import sogoing.backend_server.app.user.entity.User

class FeedbackRequestDto {

    data class CreateForm(
        val description: String,
        @Schema(description = "평점", example = "4", minimum = "1")
        @Min(1, message = "평점은 최소 1 이상이어야 합니다.")
        val rating: Int,
        val busy: Busy? = null,
        val mood: Mood? = null,
    ) {
        fun createFeedback(department: Department, user: User): Feedback {
            return Feedback(
                description = description,
                busy = busy,
                rating = rating,
                mood = mood,
                department = department,
                user = user
            )
        }
    }

    data class UpdateForm(
        val description: String? = null,
        @Schema(description = "평점", example = "4", minimum = "1")
        @Min(1, message = "평점은 최소 1 이상이어야 합니다.")
        val rating: Int? = null,
        val busy: Busy? = null,
        val mood: Mood? = null,
    ) {
        fun updateFeedback(feedback: Feedback) {
            feedback.description = description ?: feedback.description
            feedback.rating = rating ?: feedback.rating
            feedback.busy = busy ?: feedback.busy
            feedback.mood = mood ?: feedback.mood
        }
    }
}
