package sogoing.backend_server.app.department.dto.reseponse

import sogoing.backend_server.app.department.entity.Department

data class TopDepartmentResponseDto(val topDepartments: List<TopDepartmentInfo>)

data class TopDepartmentInfo(val departmentId: Long, val content: TopContent) {
    companion object {
        fun convertToDto(department: Department): TopDepartmentInfo {
            return TopDepartmentInfo(
                departmentId = department.id!!,
                content = TopContent(dibs = department.dibs.size, name = department.name)
            )
        }
    }
}

data class TopContent(
    val name: String?,
    val dibs: Int,
)
