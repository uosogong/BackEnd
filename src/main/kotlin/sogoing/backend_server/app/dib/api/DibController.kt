package sogoing.backend_server.app.dib.api

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sogoing.backend_server.app.dib.service.DibService

@RestController
@RequestMapping("/dibs")
class DibController(
    private val dibService: DibService,
) {
    @PostMapping("/{departmentId}")
    fun checkDibs(@PathVariable departmentId: Long, @AuthenticationPrincipal userId: Long): Long? {
        return dibService.changeDibs(departmentId, userId)
    }
}
