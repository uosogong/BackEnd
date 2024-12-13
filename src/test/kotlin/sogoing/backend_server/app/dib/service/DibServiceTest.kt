package sogoing.backend_server.app.dib.service

import io.mockk.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.data.repository.findByIdOrNull
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.department.repository.DepartmentRepository
import sogoing.backend_server.app.department.service.DepartmentService
import sogoing.backend_server.app.dib.dao.DibDao
import sogoing.backend_server.app.dib.entity.Dib
import sogoing.backend_server.app.dib.repository.DibRepository
import sogoing.backend_server.app.user.entity.User

class DibServiceTest {
    private val dibRepository = mockk<DibRepository>()
    private val departmentRepository = mockk<DepartmentRepository>()
    private val dibDao = mockk<DibDao>()

    private val dibService =  DibService(
        dibRepository = dibRepository,
        departmentRepository = departmentRepository,
        dibDao = dibDao,
        userRepository = mockk()
    )

    @Test
    fun getDib() {
        val mockuser1 = mockk<User>()
        val department1 = Department(
            id = 1L,
            name = "Dept A",
            introduction = "aa",
            internRecruitment = false,
            scholarshipRecruitment = false,
            user = mockuser1
        )
        every { departmentRepository.findByIdOrNull(1L) } returns department1
        every { dibDao.getDibFromUserIdAndDepartmentId(1,1) } returns Dib(
            department = department1,
            user = mockuser1
        )


        val result = dibService.getDib(1,1)

        kotlin.test.assertNotNull(result.userDib)
    }
}