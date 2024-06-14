package com.github.soup.config

import com.github.soup.auth.application.token.TokenService
import com.github.soup.auth.infra.filter.AuthFilter
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class JwtSecurityConfig(
    private val tokenService: TokenService
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
    override fun configure(http: HttpSecurity) {
        http.addFilterBefore(
            AuthFilter(tokenService),
            UsernamePasswordAuthenticationFilter::class.java
        )
    }
}