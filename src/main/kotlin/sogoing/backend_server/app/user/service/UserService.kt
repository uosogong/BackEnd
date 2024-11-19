package sogoing.backend_server.app.user.service

import org.springframework.stereotype.Service
import sogoing.backend_server.app.user.repository.UserRepository

@Service class UserService(private val userRepository: UserRepository) {}
