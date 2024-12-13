package sogoing.backend_server.app.resume.service

import io.mockk.mockk
import java.util.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import sogoing.backend_server.app.department.service.DepartmentService
import sogoing.backend_server.app.resume.dao.ResumeDao
import sogoing.backend_server.app.resume.dto.ResumeCreateRequest
import sogoing.backend_server.app.resume.entity.Resume
import sogoing.backend_server.app.resume.repository.ResumeRepository
import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.app.user.service.UserService

@ExtendWith(MockitoExtension::class)
class ResumeServiceTest {

    @Mock private lateinit var resumeRepository: ResumeRepository

    @Mock private lateinit var departmentService: DepartmentService

    @Mock private lateinit var userService: UserService

    @Mock private lateinit var resumeDao: ResumeDao

    @InjectMocks private lateinit var resumeService: ResumeService

    @Test
    fun `createResume should save resume`() {
        // Arrange
        val userId = 1L
        val departmentId = 2L
        val user = User(id = userId, name = "Test User", department = null)
        val request =
            ResumeCreateRequest(
                isScholarshipResume = true,
                isInternResume = false,
                phone = "010-1234-5678",
                address = "123 Test Street",
                workplace = "Test Company",
                totalWorkSemester = "3",
                otherScholarship = "None",
                email = "test@example.com",
                schedule = "Monday to Friday",
                content = "Resume content for scholarship",
                birthday = "2000-01-01"
            )
        val resume = Resume.from(request, mockk(), user)

        given(userService.getUserById(userId)).willReturn(user)
        given(departmentService.getDepartmentById(departmentId)).willReturn(mockk())
        given(resumeRepository.save(any(Resume::class.java))).willReturn(resume)

        // Act
        resumeService.createResume(userId, departmentId, request)

        // Assert
        then(resumeRepository).should().save(any(Resume::class.java))
    }

    @Test
    fun `findResume should throw NotFoundException when user department is null`() {
        // Arrange
        val userId = 1L
        val user = User(id = userId, name = "Test User", department = null)

        given(userService.getUserById(userId)).willReturn(user)

        // Act & Assert
        assertThrows(NotFoundException::class.java) {
            resumeService.findResume(userId, isScholarshipResume = false, isInternResume = true)
        }
    }

    @Test
    fun `findMyResume should return empty list when no resumes found`() {
        // Arrange
        val userId = 1L
        given(resumeRepository.findAllByUserId(userId)).willReturn(emptyList())

        // Act
        val responses = resumeService.findMyResume(userId)

        // Assert
        assertTrue(responses.isEmpty())
    }
}
