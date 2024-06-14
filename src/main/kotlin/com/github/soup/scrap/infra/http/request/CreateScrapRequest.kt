package com.github.soup.scrap.infra.http.request

import jakarta.validation.constraints.NotEmpty

data class CreateScrapRequest(
    @NotEmpty
    val groupId: String
)
