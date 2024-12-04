package sogoing.backend_server.app.dib.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sogoing.backend_server.app.department.repository.DepartmentRepository
import sogoing.backend_server.app.dib.dao.DibDao
import sogoing.backend_server.app.dib.dto.*
import sogoing.backend_server.app.dib.entity.Dib
import sogoing.backend_server.app.dib.repository.DibRepository
import sogoing.backend_server.app.user.repository.UserRepository
import sogoing.backend_server.common.error.exception.department.DepartmentNotFoundException
import sogoing.backend_server.common.error.exception.user.UserNotFoundException

@Service
@Transactional(readOnly = true)
class DibService(
    private val dibRepository: DibRepository,
    private val dibDao: DibDao,
    private val userRepository: UserRepository,
    private val departmentRepository: DepartmentRepository
) {
    @Transactional
    fun changeDibs(departmentId: Long, userId: Long): DibChangeResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        val userDibs = dibDao.getDibFromUserIdAndDepartmentId(userId, departmentId)

        if (userDibs != null) {
            dibRepository.delete(userDibs)
            return DibChangeResponse(false)
        }

        val department =
            departmentRepository.findByIdOrNull(departmentId) ?: throw DepartmentNotFoundException()
        val newUserDibs = Dib.create(user, department)
        dibRepository.save(newUserDibs)
        return DibChangeResponse(true)
    }

    fun getDib(departmentId: Long, userId: Long): DibStatusResponse {
        departmentRepository.findByIdOrNull(departmentId) ?: throw DepartmentNotFoundException()
        val userDibs = dibDao.getDibFromUserIdAndDepartmentId(userId, departmentId)

        userDibs?.let {
            return DibStatusResponse(true)
        }
        return DibStatusResponse(false)
    }

    fun getDibs(
        userId: Long,
        dibDepartmentRequest: DibDepartmentRequest
    ): DibDepartmentListResponse {
        val dibDepartmentListResponse = DibDepartmentListResponse()
        dibDepartmentRequest.requestDepartmentIdList?.forEach { departmentId ->
            departmentRepository.findByIdOrNull(departmentId) ?: throw DepartmentNotFoundException()

            val userDib = dibDao.getDibFromUserIdAndDepartmentId(userId, departmentId)

            if (userDib != null) {
                dibDepartmentListResponse.userDibStatus?.add(
                    DibDepartmentAboutUser(departmentId, true)
                )
            } else {
                dibDepartmentListResponse.userDibStatus?.add(
                    DibDepartmentAboutUser(departmentId, false)
                )
            }
        }
        return dibDepartmentListResponse
    }
}
