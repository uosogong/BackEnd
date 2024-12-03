package sogoing.backend_server.app.resume.dto

data class ResumeCreateRequest(
    val phone: String,
    val address: String,
    val workplace: String,
    val totalWorkSemester: String,
    val otherScholarship: String,
    val email: String,
    val message: String,
)
