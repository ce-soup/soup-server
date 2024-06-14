package com.github.soup.auth.application.token

import com.github.soup.auth.domain.auth.Auth
import com.github.soup.auth.domain.token.Token
import com.github.soup.auth.exceptions.NotFoundRoleException
import com.github.soup.redis.token.RedisTokenRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Key
import java.util.*

@Service
@Transactional(readOnly = true)
class TokenServiceImpl(
    @Value("\${jwt.secret}")
    private val secretKey: String,

    @Value("\${jwt.expired.access}")
    private val accessExpired: String,

    @Value("\${jwt.expired.refresh}")
    private val refreshExpired: String,

    private val tokenRepository: RedisTokenRepository
) : TokenService {
    private val key: Key
    private val accessTokenExpiredDate: Long
    private val refreshTokenExpiredDate: Long

    init {
        key = Keys.hmacShaKeyFor(secretKey.toByteArray())
        accessTokenExpiredDate = accessExpired.toLong()
        refreshTokenExpiredDate = refreshExpired.toLong()
    }

    @Transactional
    override fun create(auth: Auth): Token {
        val currentTime = Date().time

        val accessToken = Jwts.builder()
            .setClaims(setClaims(auth))
            .setSubject(auth.member.id)
            .setExpiration(Date(currentTime + accessTokenExpiredDate))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        val refreshToken = Jwts.builder()
            .setClaims(setClaims(auth))
            .setSubject(auth.member.id)
            .setExpiration(Date(currentTime + refreshTokenExpiredDate))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        return tokenRepository.save(
            auth.id!!,
            Token(
                accessToken = accessToken,
                refreshToken = refreshToken
            ),
            refreshTokenExpiredDate
        )
    }

    @Transactional
    override fun remove(auth: Auth): Boolean {
        tokenRepository.delete(auth.id!!)
        return true
    }

    override fun validation(token: String): Boolean {
        return getClaims(token).expiration.after(Date())
    }

    override fun parse(token: String): String {
        return getClaims(token).get("memberId", String::class.java)
    }

    override fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)
        if (claims["roles"] == null) {
            throw NotFoundRoleException()
        }

        val authorities = claims["roles"].toString()
            .replace("^\\[", "")
            .replace("]$", "")
            .split(", ")
            .map { SimpleGrantedAuthority(it) }
            .toList()

        return UsernamePasswordAuthenticationToken(
            User(claims.subject, "", authorities),
            "",
            authorities
        )
    }

    private fun setClaims(auth: Auth): Claims {
        val claims = Jwts.claims()
        claims["memberId"] = auth.member.id
        claims["roles"] = auth.roles.map { role -> role.type }.toList()
        return claims
    }

    private fun getClaims(token: String): Claims {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}