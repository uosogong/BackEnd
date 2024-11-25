package sogoing.backend_server.app.auth.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sogoing.backend_server.common.error.ApiResponse
import sogoing.backend_server.app.auth.dto.SignInRequest
import sogoing.backend_server.app.auth.dto.SignUpRequest
import sogoing.backend_server.app.auth.service.AuthService

@Tag(name = "회원 가입 및 로그인 API")
@RestController
@RequestMapping
class AuthController(private val authService: AuthService) {
    @Operation(summary = "회원 가입")
    @PostMapping("/signup")
    fun signUp(@RequestBody request: SignUpRequest): ApiResponse {
        return ApiResponse.success(authService.signUp(request))
    }

    @Operation(summary = "로그인")
    @PostMapping("/signin")
    fun signIn(@RequestBody request: SignInRequest): ApiResponse {
        return ApiResponse.success(authService.signIn(request))
    }
}