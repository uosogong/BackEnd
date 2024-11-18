package sogoing.backend_server.app.user.service

import sogoing.backend_server.app.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {


}