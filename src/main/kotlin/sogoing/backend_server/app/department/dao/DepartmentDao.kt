package sogoing.backend_server.app.department.dao

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import sogoing.backend_server.app.department.entity.Department
import sogoing.backend_server.app.department.entity.QDepartment.department
import sogoing.backend_server.app.dib.entity.QDib.dib

@Repository
class DepartmentDao(private val queryFactory: JPAQueryFactory) {
    fun findTop5DepartmentsByDibsCount(): List<Department>? {
        return queryFactory
            .selectFrom(department)
            .leftJoin(department.dibs, dib)
            .groupBy(department)
            .orderBy(dib.count().desc())
            .limit(5)
            .fetch()
    }
}
