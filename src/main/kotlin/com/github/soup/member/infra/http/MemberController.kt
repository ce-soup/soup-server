package com.github.soup.member.infra.http

import com.github.soup.member.application.service.MemberService
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member")
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/me")
    fun me(
        @Parameter(hidden = true) authentication: Authentication
    ) = memberService.me(authentication.name)
}