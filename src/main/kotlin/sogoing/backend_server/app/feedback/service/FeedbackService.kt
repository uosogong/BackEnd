package sogoing.backend_server.app.feedback.service

import java.lang.IllegalStateException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sogoing.backend_server.app.department.repository.DepartmentRepository
import sogoing.backend_server.app.feedback.dto.FeedbackRequestDto
import sogoing.backend_server.app.feedback.dto.FeedbackResponseDto
import sogoing.backend_server.app.feedback.entity.Feedback
import sogoing.backend_server.app.feedback.repository.FeedbackRepository
import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.app.user.repository.UserRepository

@Service
@Transactional(readOnly = true)
class FeedbackService(
    private val feedbackRepository: FeedbackRepository,
    private val userRepository: UserRepository,
    private val departmentRepository: DepartmentRepository
) {
    @Transactional
    fun createFeedback(createForm: FeedbackRequestDto.CreateForm, userId: Long) {
        val department =
            departmentRepository.findByIdOrNull(createForm.departmentId)
                ?: throw IllegalStateException("부서 없음")
        val user = userRepository.findByIdOrNull(userId) ?: throw IllegalStateException("유저 없음")

        checkUserFeedback(user)

        val feedback = createForm.createFeedback(department, user)
        feedbackRepository.save(feedback)
    }

    private fun checkUserFeedback(user: User) {
        val userFeedback = feedbackRepository.findByUser(user)
        if (userFeedback != null) {
            throw IllegalStateException("이미 리뷰를 작성함")
        }
    }

    fun findDepartmentFeedbacks(departmentId: Long?): FeedbackResponseDto.FeedbackListDto {
        val department =
            departmentRepository.findByIdOrNull(departmentId)
                ?: throw IllegalStateException("부서 없음")

        val departmentFeedbacks =
            feedbackRepository.findAllByDepartment(department)
                ?: return FeedbackResponseDto.FeedbackListDto()
        return FeedbackResponseDto.FeedbackListDto.convertToDto(departmentFeedbacks)
    }

    @Transactional
    fun patchFeedback(
        userId: Long,
        departmentId: Long?,
        updateForm: FeedbackRequestDto.UpdateForm
    ) {
        val existFeedback = getUniqueFeedback(userId, departmentId) ?: throw IllegalStateException()
        updateForm.updateFeedback(existFeedback)
    }

    private fun getUniqueFeedback(userId: Long, departmentId: Long?): Feedback? {
        val department =
            departmentRepository.findByIdOrNull(departmentId)
                ?: throw IllegalStateException("부서 없음")

        val user = User(userId)
        return feedbackRepository.findByDepartmentAndUser(department, user)
    }

    @Transactional
    fun deleteFeedback(userId: Long, feedbackId: Long) {
        val feedback =
            feedbackRepository.findByIdOrNull(feedbackId)
                ?: throw IllegalStateException("피드백이 없습니다")

        validateFeedbackUser(userId, feedback)

        feedbackRepository.delete(feedback)
    }

    private fun validateFeedbackUser(userId: Long, feedback: Feedback) {
        val user = userRepository.findByIdOrNull(userId)
        if (feedback.user != user) {
            throw IllegalStateException("유저의 리뷰가 아닙니다")
        }
    }
}
