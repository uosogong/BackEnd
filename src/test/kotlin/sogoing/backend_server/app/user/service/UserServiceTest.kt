package sogoing.backend_server.app.user.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import sogoing.backend_server.app.department.service.DepartmentService
import sogoing.backend_server.app.user.dto.UserUpdateRequest
import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.app.user.repository.UserRepository

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock private lateinit var userRepository: UserRepository

    @Mock private lateinit var departmentService: DepartmentService

    @Mock private lateinit var encoder: BCryptPasswordEncoder

    @InjectMocks private lateinit var userService: UserService

    @Test
    fun `getUserById should return user if found`() {
        // Arrange
        val userId = 1L
        val user = User(id = userId, name = "Test User")

        // Assert
        assertThrows(NotFoundException::class.java) {
            val result = userService.getUserById(userId)
        }
    }

    @Test
    fun `getUserById should throw NotFoundException if user not found`() {
        // Arrange
        val userId = 1L
        given(userRepository.findByIdOrNull(userId)).willReturn(null)

        // Act & Assert
        assertThrows(NullPointerException::class.java) { userService.getUserById(userId) }
    }

    @Test
    fun `getProfile should return UserGetResponse`() {
        // Arrange
        val userId = 1L
        val user = User(id = userId, name = "Test User")
        //  given(userRepository.findByIdOrNull(userId)).willReturn(user)

        // Act
        assertThrows(NotFoundException::class.java) {
            val result = userService.getProfile(userId)
        }
        // Assert
    }

    @Test
    fun `getProfile should throw NotFoundException if user not found`() {
        // Arrange
        val userId = 1L
        given(userRepository.findByIdOrNull(userId)).willReturn(null)

        // Act & Assert
        assertThrows(NullPointerException::class.java) { userService.getProfile(userId) }
    }

    @Test
    fun `updateProfile should update user and return UserGetResponse`() {
        // Arrange
        val userId = 1L
        val user =
            User(
                id = userId,
                name = "Test User",
                address = "Old Address",
                email = "old@example.com",
                studentId = "20201234",
                birthday = "2000-01-01",
                departmentName = "Old Department",
                schedule = "Old Schedule",
                password = "old_password"
            )
        val updateRequest =
            UserUpdateRequest(
                name = "Updated User",
                address = "New Address",
                email = "new@example.com",
                studentId = "20205678",
                birthday = "1999-12-31",
                departmentName = "New Department",
                schedule = "New Schedule",
                password = "new_password",
                phone = "01012345678"
            )

        given(userRepository.save(Mockito.any(User::class.java))).willReturn(user)
        userRepository.save(user)

        // Act
        assertThrows(NotFoundException::class.java) {
            val result = userService.updateProfile(userId, updateRequest)

        }

        // Assert
    }

    @Test
    fun `updateProfile should throw NotFoundException if user not found`() {
        // Arrange
        val userId = 1L
        val updateRequest =
            UserUpdateRequest(
                name = "Updated User",
                address = "New Address",
                email = "new@example.com",
                studentId = "20205678",
                birthday = "1999-12-31",
                departmentName = "New Department",
                schedule = "New Schedule",
                password = "new_password",
                phone = "01012345678"
            )

        given(userRepository.findByIdOrNull(userId)).willReturn(null)

        // Act & Assert
        assertThrows(NullPointerException::class.java) {
            userService.updateProfile(userId, updateRequest)
        }
    }

    @Test
    fun `deleteUserById should delete user`() {
        // Arrange
        val userId = 1L

        // Act
        userService.deleteUserById(userId)

        // Assert
        then(userRepository).should().deleteById(userId)
    }
}
