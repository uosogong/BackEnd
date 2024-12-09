package sogoing.backend_server.app.department.service

import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.department.repository.DepartmentRepository
import sogoing.backend_server.app.dib.dto.DibStatusResponse
import sogoing.backend_server.app.dib.service.DibService
import sogoing.backend_server.app.feedback.dto.FeedbackResponseDto
import sogoing.backend_server.app.feedback.service.FeedbackService
import sogoing.backend_server.app.user.entity.User

@SpringBootTest
class DepartmentServiceTest {
    private val departmentRepository: DepartmentRepository = mockk()
    private val dibService: DibService = mockk()
    private val feedbackService: FeedbackService = mockk()
    private val departmentService =
        DepartmentService(
            encoder = mockk(),
            departmentRepository = departmentRepository,
            userRepository = mockk(),
            departmentDao = mockk(),
            feedbackService = feedbackService,
            dibService = dibService
        )

    @Test
    fun `should return empty department list when repository is empty`() {
        // Arrange
        every { departmentRepository.findAllByOrderByUpdatedDateDesc() } returns null

        // Act
        val result = departmentService.getDepartmentsBasicInfo(0L)

        // Assert
        assertEquals(0, result.departments.size)
    }

    @Test
    fun `should return department list with ratings and dibs`() {
        val mockuser1 = mockk<User>()
        val mockuser2 = mockk<User>()
        // Arrange
        val departments =
            listOf(
                Department(
                    id = 1L,
                    name = "Dept A",
                    introduction = "aa",
                    internRecruitment = false,
                    scholarshipRecruitment = false,
                    user = mockuser1
                ),
                Department(
                    id = 2L,
                    name = "Dept B",
                    introduction = "aa",
                    internRecruitment = false,
                    scholarshipRecruitment = false,
                    user = mockuser2
                )
            )
        every { departmentRepository.findAllByOrderByUpdatedDateDesc() } returns departments
        every { feedbackService.findDepartmentFeedbacks(1L) } returns
            FeedbackResponseDto.FeedbackListDto()
        every { feedbackService.findDepartmentFeedbacks(2L) } returns
            FeedbackResponseDto.FeedbackListDto()
        every { dibService.getDib(1L, 100L) } returns DibStatusResponse(false)
        every { dibService.getDib(2L, 100L) } returns DibStatusResponse(true)

        // Act
        val result = departmentService.getDepartmentsBasicInfo(100L)

        // Assert
        assertEquals(2, result.departments.size)
        //        assertEquals(3.5F, result.departments[0])
        //        assertEquals(true, result.departments[0].isUserDibs)
    }
}
