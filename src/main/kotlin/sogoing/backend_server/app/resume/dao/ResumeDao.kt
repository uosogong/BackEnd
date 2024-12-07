package sogoing.backend_server.app.resume.dao

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import sogoing.backend_server.app.department.entity.QDepartment.department
import sogoing.backend_server.app.resume.entity.QResume.resume
import sogoing.backend_server.app.resume.entity.Resume

@Repository
class ResumeDao(private val queryFactory: JPAQueryFactory) {

    fun findByDepartmentIdAndResumeType(
        departmentId: Long,
        isScholarshipResume: Boolean,
        isInternResume: Boolean
    ): List<Resume>? {
        val conditions = mutableListOf<BooleanExpression>()

        if (isInternResume) {
            conditions.add(resume.isInternResume.eq(true))
        }

        if (isScholarshipResume) {
            conditions.add(resume.isScholarshipResume.eq(true))
        }

        val combinedCondition =
            if (conditions.isNotEmpty()) {
                conditions.reduce { acc, condition -> acc.or(condition) }
            } else {
                null
            }

        return queryFactory
            .selectFrom(resume)
            .where(combinedCondition, department.id.eq(departmentId))
            .fetch()
    }
}
