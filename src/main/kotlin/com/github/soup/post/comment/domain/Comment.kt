package com.github.soup.post.comment.domain

import com.github.soup.common.domain.Core
import com.github.soup.member.domain.Member
import com.github.soup.post.comment.infra.http.response.CommentResponse
import com.github.soup.post.domain.Post
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize

@Entity
class Comment(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    var writer: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post,

    @Column(nullable = false)
    var content: String,

    ) : Core() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Comment? = null

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "parent")
    var child: MutableList<Comment> = mutableListOf()

    fun addChild(comment: Comment) {
        comment.parent = this
        child.add(comment)
    }

    fun toResponse(): CommentResponse {
        return CommentResponse(
            id = id.toString(),
            writer = writer.toResponse(),
            content = content,
            child = if (child.size > 0) {
                child.map { c -> c.toResponse() }
            } else null,
            createdAt = createdAt!!,
            updatedAt = updatedAt!!
        )
    }
}