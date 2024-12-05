package sogoing.backend_server.app.dib.dto

import io.swagger.v3.oas.annotations.media.Schema

data class DibDepartmentRequest(
    @Schema(description = "부서ID", example = "[1,3]")
    val requestDepartmentIdList: MutableList<Long>? = mutableListOf()
)
