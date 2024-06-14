package com.github.soup.config

import com.github.soup.auth.application.token.TokenServiceImpl
import com.github.soup.auth.infra.filter.JwtAccessDeniedHandler
import com.github.soup.auth.infra.filter.JwtAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenService: TokenServiceImpl,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf().disable()

            .cors()
            .configurationSource(corsConfigurationSource())
            .and()

            .apply(JwtSecurityConfig(tokenService))
            .and()

            .exceptionHandling()
            .accessDeniedHandler(jwtAccessDeniedHandler)
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()

            .authorizeHttpRequests()
            .requestMatchers("/").permitAll()

            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/swagger-resources/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()

            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/auth/reissue").authenticated()
            .anyRequest().authenticated()
            .and()

            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.addAllowedHeader("*")
        configuration.addAllowedMethod("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }
}