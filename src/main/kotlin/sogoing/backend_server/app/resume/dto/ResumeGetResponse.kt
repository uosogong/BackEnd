package sogoing.backend_server.app.resume.dto

import java.time.LocalDateTime
import sogoing.backend_server.app.resume.entity.Resume
import sogoing.backend_server.app.user.entity.User

data class ResumeGetResponse(
    val name: String?,
    val birthday: String?,
    val studentId: String?,
    val departmentName: String?,
    val isScholarshipResume: Boolean,
    val isInternResume: Boolean,
    val phone: String,
    val address: String,
    val workplace: String,
    val totalWorkSemester: String,
    val otherScholarship: String,
    val email: String,
    val schedule: String,
    val content: String,
    val createdDate: LocalDateTime,
) {
    companion object {
        fun from(resume: Resume, user: User): ResumeGetResponse {
            return ResumeGetResponse(
                name = resume.department.name,
                birthday = user.birthday,
                studentId = user.studentId,
                departmentName = user.departmentName,
                isScholarshipResume = resume.isScholarshipResume,
                isInternResume = resume.isInternResume,
                phone = resume.phone,
                address = resume.address,
                workplace = resume.workplace,
                totalWorkSemester = resume.totalWorkSemester,
                otherScholarship = resume.otherScholarship,
                email = resume.email,
                schedule = resume.schedule,
                content = resume.content,
                createdDate = resume.createdDate!!
            )
        }

        fun from(resume: Resume): ResumeGetResponse {
            return ResumeGetResponse(
                name = resume.user.name,
                birthday = resume.birthday,
                studentId = resume.user.studentId,
                departmentName = resume.user.departmentName,
                isScholarshipResume = resume.isScholarshipResume,
                isInternResume = resume.isInternResume,
                phone = resume.phone,
                address = resume.address,
                workplace = resume.workplace,
                totalWorkSemester = resume.totalWorkSemester,
                otherScholarship = resume.otherScholarship,
                email = resume.email,
                schedule = resume.schedule,
                content = resume.content,
                createdDate = resume.createdDate!!
            )
        }
    }
}
