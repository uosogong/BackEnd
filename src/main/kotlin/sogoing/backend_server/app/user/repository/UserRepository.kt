package sogoing.backend_server.app.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import sogoing.backend_server.app.user.entity.User

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}