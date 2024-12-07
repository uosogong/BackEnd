package sogoing.backend_server.app.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import sogoing.backend_server.app.user.entity.User

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    @Modifying
    @Query("UPDATE User u SET u.deletedDate = current_timestamp WHERE u.id = :userId")
    override fun deleteById(userId: Long)
}
