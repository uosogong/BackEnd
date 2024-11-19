package sogoing.backend_server.app.feedback.dto

import sogoing.backend_server.app.feedback.entity.Feedback

class FeedbackResponseDto {
    data class FeedbackListDto(
        val feedbackList: List<FeedbackDto>? = null,
    ) {
        companion object {
            fun convertToDto(feedbackList: List<Feedback>): FeedbackListDto {
                val feedbackDtos =
                    feedbackList.stream().map { FeedbackDto.convertToDto(it) }.toList()
                return FeedbackListDto(feedbackDtos)
            }
        }
    }

    data class FeedbackDto(
        val description: String? = null,
        val rating: Int? = null,
        val busy: String? = null,
        val mood: String? = null,
    ) {
        companion object {
            fun convertToDto(feedback: Feedback): FeedbackDto {
                return FeedbackDto(
                    feedback.description,
                    feedback.rating,
                    feedback.busy,
                    feedback.mood
                )
            }
        }
    }
}
