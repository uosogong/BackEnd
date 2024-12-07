package sogoing.backend_server.app.user.api

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sogoing.backend_server.app.user.dto.UserUpdateRequest
import sogoing.backend_server.app.user.service.UserService
import sogoing.backend_server.common.error.ApiResponse

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getProfile(@AuthenticationPrincipal userDetails: UserDetails): ApiResponse {
        return ApiResponse.success(userService.getProfile(userDetails.username.toLong()))
    }

    @PatchMapping
    fun updateProfile(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody request: UserUpdateRequest
    ): ApiResponse {
        return ApiResponse.success(
            userService.updateProfile(userDetails.username.toLong(), request)
        )
    }

    @DeleteMapping
    fun deleteUser(@AuthenticationPrincipal userDetails: UserDetails): ApiResponse {
        return ApiResponse.success(userService.deleteUserById(userDetails.username.toLong()))
    }
}
