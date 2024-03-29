package com.github.soup.post.infra.http.request

import com.github.soup.post.domain.PostTypeEnum
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

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
