package sogoing.backend_server.app.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.sql.Date
import java.time.Instant

@Service
class TokenProvider(
    @Value("\${jwt.access.secret}")
    private val accessSecret: String,
    @Value("\${jwt.access.expiration}")
    private val expirationHours: Long,
) {
    private val signingKey = Keys.hmacShaKeyFor(accessSecret.toByteArray())

    fun createToken(userSpecification: String): String {
        val now = Instant.now()
        val expiration = now.plusSeconds(expirationHours * 3600)

        return Jwts.builder()
            .subject(userSpecification)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(signingKey)
            .compact()
    }

    fun validateTokenAndGetSubject(token: String?): String? {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }
}
