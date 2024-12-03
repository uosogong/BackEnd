package sogoing.backend_server.app.feedback.api

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
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
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody createForm: FeedbackRequestDto.CreateForm,
    ): ResponseEntity<Boolean> {
        val isFeedbackCreated =
            feedbackService.createFeedback(departmentId, createForm, userDetails.username.toLong())
        return ResponseEntity.ok(isFeedbackCreated)
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
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody updateForm: FeedbackRequestDto.UpdateForm
    ) {
        feedbackService.patchFeedback(userDetails.username.toLong(), departmentId, updateForm)
    }

    @DeleteMapping("/{feedbackId}")
    fun deleteFeedback(
        @PathVariable feedbackId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ) {
        feedbackService.deleteFeedback(userDetails.username.toLong(), feedbackId)
    }
}
