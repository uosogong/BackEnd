package sogoing.backend_server.app.resume.api

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import sogoing.backend_server.app.resume.dto.ResumeCreateRequest
import sogoing.backend_server.app.resume.dto.ResumeGetRequest
import sogoing.backend_server.app.resume.service.ResumeService
import sogoing.backend_server.common.error.ApiResponse

@RestController
@RequestMapping("/resumes")
class ResumeController(private val resumeService: ResumeService) {

    @PostMapping("/{departmentId}")
    fun createResume(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable departmentId: Long,
        @RequestBody requestBody: ResumeCreateRequest,
    ): ApiResponse {
        return ApiResponse.success(
            resumeService.createResume(userDetails.username.toLong(), departmentId, requestBody)
        )
    }

    @GetMapping
    fun findResume(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam requestBody: ResumeGetRequest,
    ): ApiResponse {
        return ApiResponse.success(resumeService.findResume(userDetails.username.toLong(), requestBody))
    }
}
