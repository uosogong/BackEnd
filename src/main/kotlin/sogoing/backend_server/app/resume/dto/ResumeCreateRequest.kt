package sogoing.backend_server.app.resume.dto

data class ResumeCreateRequest(
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
)
