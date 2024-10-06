package sogoing.backend_server.app.user.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

  @GetMapping("/")
  fun testCall(): String {
    return "success"
  }
}
