package sogoing.backend_server.app.department.service

import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.department.repository.DepartmentRepository

@Service
class DepartmentService(
    private val departmentRepository: DepartmentRepository,
) {

    @Transactional
    fun getDepartmentByName(name: String): Department {
        return departmentRepository.findByName(name) ?: throw NotFoundException()
    }
}
