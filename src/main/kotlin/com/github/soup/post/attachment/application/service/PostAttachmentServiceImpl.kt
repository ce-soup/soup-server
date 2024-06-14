package com.github.soup.post.attachment.application.service

import com.github.soup.post.attachment.domain.PostAttachment
import com.github.soup.post.attachment.infra.persistence.PostAttachmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostAttachmentServiceImpl(
    private val postAttachmentRepository: PostAttachmentRepository
) : PostAttachmentService {

    @Transactional
    override fun save(postAttachment: PostAttachment): PostAttachment {
        return postAttachmentRepository.save(postAttachment)
    }
}