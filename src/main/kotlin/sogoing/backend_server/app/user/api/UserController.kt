package sogoing.backend_server.app.user.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

  @GetMapping("/{test}")
  fun testCall(@PathVariable test: String): String {
    return "success - $test"
  }
}
