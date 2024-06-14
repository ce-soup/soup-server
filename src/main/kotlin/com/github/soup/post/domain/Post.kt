package com.github.soup.post.domain

import com.github.soup.common.domain.Core
import com.github.soup.group.domain.Group
import com.github.soup.member.domain.Member
import com.github.soup.post.attachment.domain.PostAttachment
import com.github.soup.post.infra.http.request.UpdatePostRequest
import com.github.soup.post.infra.http.response.PostResponse
import jakarta.persistence.*

@Entity
class Post(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    var group: Group,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    var writer: Member,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: PostTypeEnum,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var content: String,

    @OneToMany(
        targetEntity = PostAttachment::class,
        mappedBy = "post",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var attachments: MutableList<PostAttachment>? = mutableListOf()

) : Core() {

    fun update(request: UpdatePostRequest): Post {
        type = request.type
        title = request.title
        content = request.content
        return this
    }

    fun toResponse(): PostResponse {
        return PostResponse(
            id = id.toString(),
            writer = writer.toResponse(),
            group = group.toResponse(),
            type = type,
            title = title,
            content = content,
            createdAt = createdAt!!,
            updatedAt = updatedAt!!,
            postAttachments = attachments?.map { it.file.toResponse() }
        )
    }
}
