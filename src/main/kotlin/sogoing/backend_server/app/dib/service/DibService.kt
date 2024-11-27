package sogoing.backend_server.app.dib.service

import java.lang.IllegalStateException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.department.repository.DepartmentRepository
import sogoing.backend_server.app.dib.dao.DibDao
import sogoing.backend_server.app.dib.entity.Dib
import sogoing.backend_server.app.dib.repository.DibRepository
import sogoing.backend_server.app.user.repository.UserRepository

@Service
class DibService(
    private val dibRepository: DibRepository,
    private val dibDao: DibDao,
    private val userRepository: UserRepository,
    private val departmentRepository: DepartmentRepository
) {
    fun changeDibs(departmentId: Long, userId: Long): Long? {
        val user = userRepository.findByIdOrNull(userId) ?: throw IllegalStateException()
        val userDibs = dibDao.getDibFromUserIdAndDepartmentId(userId, departmentId)

        if (userDibs != null) {
            dibRepository.delete(userDibs)
            return userDibs.id
        }

        val department = departmentRepository.findByIdOrNull(departmentId) ?: throw IllegalStateException("부서 없음")
        val newUserDibs = Dib.create(user, department)
        dibRepository.save(newUserDibs)
        return newUserDibs.id
    }
}
