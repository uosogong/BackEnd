package sogoing.backend_server.app.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import sogoing.backend_server.app.user.entity.UserRole

data class SignResponse(
    @Schema(description = "회원 이름", example = "신짱구") val name: String?,
    @Schema(description = "회원 권한", example = "USER") val role: UserRole?,
    @Schema(
        description = "Access Token",
        example =
            "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxOlVTRVIiLCJpYXQiOjE3MzIyNTI4ODgsImV4cCI6MTczNDQxMjg4OH0.yK6SIzUk37RID3sCjm628cOG0I-mKqOzjFSyxgmQKvSbJgseXTWIZGu63EYMQVgo"
    )
    val token: String
)
