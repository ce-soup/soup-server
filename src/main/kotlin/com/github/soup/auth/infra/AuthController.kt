package com.github.soup.auth.infra

import com.github.soup.auth.application.auth.AuthServiceImpl
import com.github.soup.auth.infra.http.request.ReIssueRequest
import com.github.soup.auth.infra.http.request.SignInRequest
import com.github.soup.auth.infra.http.request.SignUpRequest
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController(
	private val authService: AuthServiceImpl
) {
	@PostMapping("/login")
	fun login(@RequestBody @Valid request: SignInRequest) = authService.login(request)

	@PostMapping("/new")
	fun new(@RequestBody @Valid request: SignUpRequest) = authService.create(request)

	@PostMapping("/reissue")
	fun reissue(@RequestBody @Valid request: ReIssueRequest) = authService.reissue(request)

	@PostMapping("/delete")
	fun logout(@ApiIgnore authentication: Authentication) = authService.logout(authentication.name)
}