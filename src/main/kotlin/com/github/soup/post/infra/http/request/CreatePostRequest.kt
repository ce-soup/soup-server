package com.github.soup.post.infra.http.request

import com.github.soup.post.domain.PostTypeEnum
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

data class CreatePostRequest(
    @NotEmpty
    val groupId: String,

    @NotNull
    val type: PostTypeEnum,

    @NotEmpty
    val title: String,

    @NotEmpty
    val content: String,

    val images: List<MultipartFile>?
)
