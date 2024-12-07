package sogoing.backend_server.app.resume.dto

import java.time.LocalDateTime
import sogoing.backend_server.app.resume.entity.Resume

data class ResumeGetResponse(
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
        fun from(resume: Resume): ResumeGetResponse {
            return ResumeGetResponse(
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
