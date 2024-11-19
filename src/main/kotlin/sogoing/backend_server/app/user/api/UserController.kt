package sogoing.backend_server.app.user.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import sogoing.backend_server.app.user.service.UserService

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/{test}")
    fun testCall(@PathVariable test: String): String {
        return "success ver 2 - $test"
    }
}
