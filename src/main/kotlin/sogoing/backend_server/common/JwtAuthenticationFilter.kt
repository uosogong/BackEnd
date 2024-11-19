package sogoing.backend_server.common

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
//    private val jwtProvider: JwtProvider // JWT 생성 및 검증 유틸
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolveToken(request) ?: "1"
        val authentication =
            UsernamePasswordAuthenticationToken(
                token.toLong(), // Principal로 userId를 사용
                null, // Credentials는 null
                emptyList() // 권한 정보 (비어 있음)
            )
        println(token)
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7) // "Bearer " 이후의 토큰만 반환
        } else null
    }
}
