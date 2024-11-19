package sogoing.backend_server.app.feedback.api

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import sogoing.backend_server.app.feedback.dto.FeedbackRequestDto
import sogoing.backend_server.app.feedback.dto.FeedbackResponseDto
import sogoing.backend_server.app.feedback.service.FeedbackService

@RestController
@RequestMapping("/feedbacks")
class FeedbackController(private val feedbackService: FeedbackService) {

    @PostMapping("/{departmentId}")
    fun createFeedback(
        @PathVariable departmentId: Long?,
        @AuthenticationPrincipal userId: Long,
        @RequestBody createForm: FeedbackRequestDto.CreateForm,
    ): ResponseEntity<Unit> {
        println(userId)
        feedbackService.createFeedback(createForm, userId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{departmentId}")
    fun getFeedbacks(
        @PathVariable departmentId: Long?
    ): ResponseEntity<FeedbackResponseDto.FeedbackListDto> {
        return ResponseEntity.ok(feedbackService.findDepartmentFeedbacks(departmentId))
    }

    @PatchMapping("/{departmentId}")
    fun patchFeedback(
        @PathVariable departmentId: Long?,
        @AuthenticationPrincipal userId: Long,
        @RequestBody updateForm: FeedbackRequestDto.UpdateForm
    ) {
        feedbackService.patchFeedback(userId, departmentId, updateForm)
    }

    @DeleteMapping("/{feedbackId}")
    fun deleteFeedback(
        @PathVariable feedbackId: Long,
        @AuthenticationPrincipal userId: Long,
    ) {
        feedbackService.deleteFeedback(userId, feedbackId)
    }
}
