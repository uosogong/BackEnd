package sogoing.backend_server.app.feedback.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sogoing.backend_server.app.department.repository.DepartmentRepository
import sogoing.backend_server.app.feedback.dto.FeedbackRequestDto
import sogoing.backend_server.app.feedback.dto.FeedbackResponseDto
import sogoing.backend_server.app.feedback.entity.Feedback
import sogoing.backend_server.app.feedback.repository.FeedbackRepository
import sogoing.backend_server.app.user.repository.UserRepository
import sogoing.backend_server.common.error.exception.department.DepartmentNotFoundException
import sogoing.backend_server.common.error.exception.feedback.FeedbackNotFoundException
import sogoing.backend_server.common.error.exception.user.UserNotFoundException

@Service
@Transactional(readOnly = true)
class FeedbackService(
    private val feedbackRepository: FeedbackRepository,
    private val userRepository: UserRepository,
    private val departmentRepository: DepartmentRepository
) {
    @Transactional
    fun createFeedback(
        departmentId: Long?,
        createForm: FeedbackRequestDto.CreateForm,
        userId: Long
    ): Boolean {
        val department =
            departmentRepository.findByIdOrNull(departmentId) ?: throw DepartmentNotFoundException()
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()

        try {
            getUserFeedback(userId, departmentId)
        } catch (e: FeedbackNotFoundException) {
            val feedback = createForm.createFeedback(department, user)
            feedbackRepository.save(feedback)
            return true
        }

        return false
    }

    fun findDepartmentFeedbacks(departmentId: Long?): FeedbackResponseDto.FeedbackListDto {
        val department =
            departmentRepository.findByIdOrNull(departmentId) ?: throw DepartmentNotFoundException()

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
        val existFeedback = getUserFeedback(userId, departmentId)
        updateForm.updateFeedback(existFeedback)
    }

    private fun getUserFeedback(userId: Long, departmentId: Long?): Feedback {
        val department =
            departmentRepository.findByIdOrNull(departmentId) ?: throw DepartmentNotFoundException()

        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        return feedbackRepository.findByDepartmentAndUser(department, user)
            ?: throw FeedbackNotFoundException()
    }

    @Transactional
    fun deleteFeedback(userId: Long, feedbackId: Long) {
        val feedback =
            feedbackRepository.findByIdOrNull(feedbackId) ?: throw FeedbackNotFoundException()

        validateFeedbackUser(userId, feedback)

        feedbackRepository.delete(feedback)
    }

    private fun validateFeedbackUser(userId: Long, feedback: Feedback) {
        val user = userRepository.findByIdOrNull(userId)
        if (feedback.user != user) {
            throw FeedbackNotFoundException()
        }
    }
}
