package sogoing.backend_server.common

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import sogoing.backend_server.app.auth.JwtAuthenticationFilter

@EnableMethodSecurity
@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val entryPoint: AuthenticationEntryPoint
) {
    private val allowedUris = arrayOf("/signup", "/signin")

    @Bean
    fun filterChain(http: HttpSecurity) =
        http
            .csrf { it.disable() }
            .cors { }
            .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } }
            .authorizeHttpRequests {
                it.requestMatchers(*allowedUris).permitAll().anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter::class.java)
            .exceptionHandling { it.authenticationEntryPoint(entryPoint) }
            .build()!!

    @Bean fun passwordEncoder() = BCryptPasswordEncoder()

    @Configuration
    class WebConfig : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/**")
                .allowedOrigins("https://www.sogoing.kro.kr/", "http://localhost:5173/") // 허용할 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // 허용할 HTTP 메서드
        }
    }
}
