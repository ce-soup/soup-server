package com.github.soup.auth.domain.token

import com.github.soup.auth.infra.http.response.TokenResponse
import jakarta.validation.constraints.NotBlank
import java.io.Serializable

class Token(
    @NotBlank
    val accessToken: String,

    @NotBlank
    val refreshToken: String,
) : Serializable {
    constructor() : this("", "")

    fun toResponse(): TokenResponse {
        return TokenResponse(
            accessToken = this.accessToken,
            refreshToken = this.refreshToken
        )
    }
}