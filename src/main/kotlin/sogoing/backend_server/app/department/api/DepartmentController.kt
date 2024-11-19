package sogoing.backend_server.app.department.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DepartmentController {
    @GetMapping("/api/ping")
    fun registerDepartment(): String {
        return "pong"
    }
}
