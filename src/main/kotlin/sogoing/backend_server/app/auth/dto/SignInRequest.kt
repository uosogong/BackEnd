package sogoing.backend_server.app.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

data class SignInRequest(
    @Schema(description = "회원 이메일", example = "foo@bar.com")
    val email: String,
    @Schema(description = "회원 비밀번호", example = "1234")
    val password: String
)