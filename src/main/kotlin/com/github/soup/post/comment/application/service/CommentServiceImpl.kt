package com.github.soup.post.comment.application.service

import com.github.soup.post.comment.domain.Comment
import com.github.soup.post.comment.exception.NotFoundCommentException
import com.github.soup.post.comment.infra.persistence.CommentRepository
import com.github.soup.post.domain.Post
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentServiceImpl(
    private val commentRepository: CommentRepository
) : CommentService {

    @Transactional
    override fun save(comment: Comment): Comment {
        return commentRepository.save(comment)
    }

    override fun getById(commentId: String): Comment {
        return commentRepository.getById(commentId).orElseThrow { NotFoundCommentException() }
    }

    override fun getByPost(post: Post): List<Comment> {
        return commentRepository.getByPost(post)
    }

    @Transactional
    override fun deleteById(comment: Comment) {
        if (comment.child.size > 0) {
            commentRepository.deleteAllInBatch(comment.child)
        }
        return commentRepository.delete(comment)
    }

    @Transactional
    override fun deleteByPost(post: Post) {
        val comments: List<Comment> = commentRepository.getByPost(post)
        comments.forEach { c -> if (c.child.size > 0) commentRepository.deleteAllInBatch(c.child) }
        commentRepository.deleteAllInBatch(comments)
    }
}