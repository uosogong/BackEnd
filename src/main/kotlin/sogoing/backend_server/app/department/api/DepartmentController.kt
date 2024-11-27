package sogoing.backend_server.app.department.api

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sogoing.backend_server.app.department.dto.request.DepartmentUpdateRequestDto
import sogoing.backend_server.app.department.dto.reseponse.DepartmentBasicResponseDto
import sogoing.backend_server.app.department.dto.reseponse.DepartmentDetailResponseDto
import sogoing.backend_server.app.department.dto.reseponse.DepartmentUpdateResponseDto
import sogoing.backend_server.app.department.dto.reseponse.TopDepartmentResponseDto
import sogoing.backend_server.app.department.service.DepartmentService

@RestController
@RequestMapping("/departments")
class DepartmentController(private val departmentService: DepartmentService) {

    @GetMapping
    fun getDepartmentsBasicInfo(
        @AuthenticationPrincipal userId: Long
    ): ResponseEntity<DepartmentBasicResponseDto> {
        return ResponseEntity.ok(departmentService.getDepartmentsBasicInfo(userId))
    }

    @GetMapping("/{departmentId}")
    fun getDepartmentDetail(
        @PathVariable departmentId: Long,
        @AuthenticationPrincipal userId: Long
    ): ResponseEntity<DepartmentDetailResponseDto> {
        return ResponseEntity.ok(departmentService.getDepartmentInfoWithFeedback(departmentId))
    }

    @PatchMapping("/{departmentId}")
    fun updateDepartmentStatus(
        @PathVariable departmentId: Long,
        @AuthenticationPrincipal userId: Long,
        @RequestBody departmentUpdateRequestDto: DepartmentUpdateRequestDto,
    ): ResponseEntity<DepartmentUpdateResponseDto> {
        return ResponseEntity.ok(
            departmentService.updateDepartmentStatus(
                userId,
                departmentId,
                departmentUpdateRequestDto
            )
        )
    }

    @GetMapping("/top")
    fun getTopDepartment(): ResponseEntity<TopDepartmentResponseDto> {
        return ResponseEntity.ok(departmentService.getTop5Department())
    }
}
