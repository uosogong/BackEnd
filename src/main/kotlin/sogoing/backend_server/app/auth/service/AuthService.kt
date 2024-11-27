package sogoing.backend_server.app.auth.service

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sogoing.backend_server.app.auth.TokenProvider
import sogoing.backend_server.app.auth.dto.SignInRequest
import sogoing.backend_server.app.auth.dto.SignResponse
import sogoing.backend_server.app.auth.dto.SignUpRequest
import sogoing.backend_server.app.user.entity.User
import sogoing.backend_server.app.user.repository.UserRepository
import sogoing.backend_server.common.error.exception.user.UserEmailAlreadyExistingException

@Service
@Transactional
class AuthService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val tokenProvider: TokenProvider
) {

    fun signUp(request: SignUpRequest): SignResponse {

        if (userRepository.existsByEmail(request.email)) {
            throw UserEmailAlreadyExistingException()
        }

        val user = User.from(request, encoder)
        userRepository.save(user)

        return signResponse(user)
    }

    fun signIn(request: SignInRequest): SignResponse {
        val user = userRepository.findByEmail(request.email) ?: throw NotFoundException()
        if (encoder.matches(request.password, user.password)) {
            return signResponse(user)
        }
        throw IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.")
    }

    private fun signResponse(user: User): SignResponse {
        val token = tokenProvider.createToken("${user.id}:${user.role}")

        return SignResponse(user.name, user.role, token)
    }
}
