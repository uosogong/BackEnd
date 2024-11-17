package sogoing.backend_server.app.user.repository

import User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
}
