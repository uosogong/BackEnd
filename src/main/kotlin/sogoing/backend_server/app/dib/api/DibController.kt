package sogoing.backend_server.app.dib.api

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sogoing.backend_server.app.dib.dto.DibChangeResponse
import sogoing.backend_server.app.dib.dto.DibStatusResponse
import sogoing.backend_server.app.dib.service.DibService

@RestController
@RequestMapping("/dibs")
class DibController(
    private val dibService: DibService,
) {
    @PostMapping("/{departmentId}")
    fun checkDibs(
        @PathVariable departmentId: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<DibChangeResponse> {
        return ResponseEntity.ok(dibService.changeDibs(departmentId, userDetails.username.toLong()))
    }

    @GetMapping("/{departmentId}")
    fun getDibs(
        @PathVariable departmentId: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<DibStatusResponse> {
        return ResponseEntity.ok(dibService.getDibs(departmentId, userDetails.username.toLong()))
    }
}
