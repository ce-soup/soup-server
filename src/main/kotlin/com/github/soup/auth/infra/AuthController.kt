package com.github.soup.auth.infra

import com.github.soup.auth.application.auth.AuthService
import com.github.soup.auth.infra.http.request.ReIssueRequest
import com.github.soup.auth.infra.http.request.SignInRequest
import com.github.soup.auth.infra.http.request.SignUpRequest
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody @Valid request: SignInRequest) = authService.login(request)

    @PostMapping("/new")
    fun new(@Valid request: SignUpRequest) = authService.create(request)

    @PostMapping("/reissue")
    fun reissue(@RequestBody @Valid request: ReIssueRequest) = authService.reissue(request)

    @PostMapping("/logout")
    fun logout(
        @Parameter(hidden = true) authentication: Authentication
    ) = authService.logout(authentication.name)
}