package com.github.soup.post.infra.http.response

import com.github.soup.member.infra.http.response.MemberResponse
import com.github.soup.post.domain.PostTypeEnum
import java.time.LocalDateTime

data class PostResponse(
    val id: String,
    val writer: MemberResponse,
    val type: PostTypeEnum,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)