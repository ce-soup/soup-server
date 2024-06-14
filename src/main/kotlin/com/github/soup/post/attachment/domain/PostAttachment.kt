package com.github.soup.post.attachment.domain

import com.github.soup.common.domain.Core
import com.github.soup.file.domain.File
import com.github.soup.post.domain.Post
import jakarta.persistence.*

@Entity
class PostAttachment(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post: Post,

    @OneToOne
    @JoinColumn(name = "file_id")
    var file: File
) : Core()
