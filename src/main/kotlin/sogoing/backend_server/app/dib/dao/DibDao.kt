package sogoing.backend_server.app.dib.dao

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import sogoing.backend_server.app.department.entity.QDepartment.department
import sogoing.backend_server.app.dib.entity.Dib
import sogoing.backend_server.app.dib.entity.QDib.dib
import sogoing.backend_server.app.user.entity.QUser.user

@Repository
class DibDao(private val queryFactory: JPAQueryFactory) {
    fun getDibFromUserIdAndDepartmentId(
        userId: Long,
        departmentId: Long,
    ): Dib? {
        return queryFactory
            .selectFrom(dib)
            .join(user)
            .on(user.eq(dib.user))
            .join(department)
            .on(department.eq(dib.department))
            .where(user.id.eq(userId).and(department.id.eq(departmentId)))
            .fetchOne()
    }
}
