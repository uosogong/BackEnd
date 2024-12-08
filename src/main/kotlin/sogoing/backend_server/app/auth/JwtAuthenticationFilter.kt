package sogoing.backend_server.app.auth

import io.jsonwebtoken.MalformedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import sogoing.backend_server.common.error.ErrorCode
import sogoing.backend_server.common.error.exception.AccessDeniedException

@Order(0)
@Component
class JwtAuthenticationFilter(private val tokenProvider: TokenProvider) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token =
                parseBearerToken(request) ?: throw AccessDeniedException(ErrorCode.TOKEN_NOT_FOUND)
            val user = parseUserSpecification(token)
            UsernamePasswordAuthenticationToken.authenticated(user, token, user.authorities)
                .apply { details = WebAuthenticationDetails(request) }
                .also { SecurityContextHolder.getContext().authentication = it }
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
            request.setAttribute("exception", e)
            throw e
        }

        filterChain.doFilter(request, response)
    }

    private fun parseBearerToken(request: HttpServletRequest): String? =
        request
            .getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith("Bearer ", true) }
            ?.substring(7)

    private fun parseUserSpecification(token: String?): User =
        token
            ?.let { tokenProvider.validateTokenAndGetSubject(it) }
            ?.split(":")
            ?.let { User(it[0], "", listOf(SimpleGrantedAuthority(it[1]))) }
            ?: throw MalformedJwtException("Token parsing failed")
}
