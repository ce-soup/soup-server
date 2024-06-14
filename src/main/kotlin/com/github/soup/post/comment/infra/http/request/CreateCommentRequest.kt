package com.github.soup.post.comment.infra.http.request

import jakarta.validation.constraints.NotEmpty

data class CreateCommentRequest(
    @NotEmpty
    val postId: String,

    val parentId: String?,

    @NotEmpty
    val content: String
)