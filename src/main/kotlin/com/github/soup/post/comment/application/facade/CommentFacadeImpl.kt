package com.github.soup.post.comment.application.facade

import com.github.soup.member.application.service.MemberService
import com.github.soup.member.domain.Member
import com.github.soup.post.appllication.service.PostService
import com.github.soup.post.comment.application.service.CommentService
import com.github.soup.post.comment.domain.Comment
import com.github.soup.post.comment.exception.NotFoundCommentAuthorityException
import com.github.soup.post.comment.infra.http.request.CreateCommentRequest
import com.github.soup.post.comment.infra.http.response.CommentResponse
import com.github.soup.post.domain.Post
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class CommentFacadeImpl(
    private val commentService: CommentService,
    private val memberService: MemberService,
    private val postService: PostService
) : CommentFacade {

    @Transactional
    override fun create(memberId: String, request: CreateCommentRequest): CommentResponse {
        val writer: Member = memberService.getByMemberId(memberId)
        val post: Post = postService.getById(request.postId)

        val comment: Comment = commentService.save(
            Comment(
                writer = writer,
                post = post,
                content = request.content
            )
        )

        if (request.parentId != null) {
            val parent: Comment = commentService.getById(request.parentId)
            parent.addChild(comment)
        }
        return comment.toResponse()
    }

    override fun getByPostId(postId: String): List<CommentResponse> {
        val post: Post = postService.getById(postId)
        return commentService.getByPost(post).map { c -> c.toResponse() }
    }

    override fun get(commentId: String): CommentResponse {
        return commentService.getById(commentId).toResponse()
    }

    @Transactional
    override fun delete(memberId: String, commentId: String): Boolean {
        val writer: Member = memberService.getByMemberId(memberId)
        val comment: Comment = commentService.getById(commentId)

        if (writer.id != comment.writer.id) {
            throw NotFoundCommentAuthorityException()
        }

        commentService.deleteById(comment)
        return true
    }

}