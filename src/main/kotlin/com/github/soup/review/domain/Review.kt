package com.github.soup.review.domain

import com.github.soup.common.domain.Core
import com.github.soup.group.domain.Group
import com.github.soup.member.domain.Member
import com.github.soup.review.infra.http.response.ReviewResponse
import jakarta.persistence.*

@Entity
class Review(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id")
    var from: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id")
    var to: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    var group: Group,

    @Column(nullable = false)
    var content: String,

    @Column(nullable = false)
    var score: Float

) : Core() {
    fun update(content: String, score: Float): Review {
        this.content = content
        this.score = score
        return this
    }

    fun toResponse(): ReviewResponse {
        return ReviewResponse(
            id = id.toString(),
            from = from.toResponse(),
            to = to.toResponse(),
            group = group.toResponse(),
            content = content,
            score = score,
            createdAt = createdAt!!,
            updatedAt = updatedAt!!,
        )
    }
}