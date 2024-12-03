package sogoing.backend_server.app.feedback.dto

import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.feedback.entity.Feedback
import sogoing.backend_server.app.user.entity.User

class FeedbackRequestDto {

    data class CreateForm(
        val description: String,
        val rating: Int,
        val busy: String? = null,
        val mood: String? = null,
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
        val rating: Int? = null,
        val busy: String? = null,
        val mood: String? = null,
    ) {
        fun updateFeedback(feedback: Feedback) {
            feedback.description = description ?: feedback.description
            feedback.rating = rating ?: feedback.rating
            feedback.busy = busy ?: feedback.busy
            feedback.mood = mood ?: feedback.mood
        }
    }
}
