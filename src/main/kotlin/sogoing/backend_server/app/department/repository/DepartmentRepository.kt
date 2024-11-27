package sogoing.backend_server.app.department.repository

import org.springframework.data.jpa.repository.JpaRepository
import sogoing.backend_server.app.department.entity.Department

interface DepartmentRepository : JpaRepository<Department, Long> {
    fun findByName(name: String): Department?

    fun findAllByOrderByUpdatedDateDesc(): List<Department>?

    fun findTop5ByOrderByDibsDesc(): List<Department>?
}
