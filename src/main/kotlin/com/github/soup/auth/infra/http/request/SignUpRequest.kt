package com.github.soup.auth.infra.http.request

import com.github.soup.auth.domain.auth.AuthType
import com.github.soup.member.domain.SexType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import org.springframework.web.multipart.MultipartFile

data class SignUpRequest(
    @NotNull
    val type: AuthType,

    @NotBlank
    val token: String,

    @NotBlank
    @Length(min = 2)
    val name: String,

    @NotBlank
    @Length(min = 2)
    val nickname: String,

    @NotNull
    val sex: SexType,

    val bio: String? = null,

    val profileImage: MultipartFile? = null
)