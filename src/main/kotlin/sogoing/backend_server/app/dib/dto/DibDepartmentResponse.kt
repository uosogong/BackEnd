package sogoing.backend_server.app.dib.dto

data class DibDepartmentListResponse(
    val userDibStatus: MutableList<DibDepartmentAboutUser>? = mutableListOf(),
)

data class DibDepartmentAboutUser(val departmentId: Long, val userDib: Boolean)
