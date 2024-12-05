package sogoing.backend_server.app.resume.dao

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import sogoing.backend_server.app.department.entity.QDepartment.department
import sogoing.backend_server.app.resume.entity.QResume.resume
import sogoing.backend_server.app.resume.entity.Resume

@Repository
class ResumeDao(private val queryFactory: JPAQueryFactory) {
    fun findByDepartmentIdAndResumeType(
        departmentId: Long,
        isScholarShipResume: Boolean,
        isInternResume: Boolean,
    ): List<Resume>? {
        return queryFactory
            .selectFrom(resume)
            .where(
                resume.isInternResume
                    .eq(isInternResume)
                    .or(resume.isScholarshipResume.eq(isScholarShipResume))
                    .and(department.id.eq(departmentId))
            )
            .fetch()
    }
}
