package com.github.soup.post.appllication.facade

import com.github.soup.file.application.facade.FileFacade
import com.github.soup.file.domain.FileType
import com.github.soup.group.application.service.GroupService
import com.github.soup.group.domain.Group
import com.github.soup.group.participant.application.service.ParticipantService
import com.github.soup.member.application.service.MemberService
import com.github.soup.member.domain.Member
import com.github.soup.post.appllication.service.PostService
import com.github.soup.post.attachment.application.service.PostAttachmentService
import com.github.soup.post.attachment.domain.PostAttachment
import com.github.soup.post.comment.application.service.CommentService
import com.github.soup.post.domain.Post
import com.github.soup.post.domain.PostTypeEnum
import com.github.soup.post.exception.NotFoundPostAuthorityException
import com.github.soup.post.infra.http.request.CreatePostRequest
import com.github.soup.post.infra.http.request.UpdatePostRequest
import com.github.soup.post.infra.http.response.PostResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class PostFacadeImpl(
    private val postService: PostService,
    private val memberService: MemberService,
    private val groupService: GroupService,
    private val participantService: ParticipantService,
    private val commentService: CommentService,
    private val fileFacade: FileFacade,
    private val attachmentService: PostAttachmentService
) : PostFacade {

    @Transactional
    override fun create(memberId: String, request: CreatePostRequest): PostResponse {
        val writer: Member = memberService.getByMemberId(memberId)
        val group: Group = groupService.getById(request.groupId)

        val post: Post = postService.save(
            Post(
                group = group,
                writer = writer,
                title = request.title,
                content = request.content,
                type = request.type
            )
        )
        if (request.images != null) {
            val files = fileFacade.uploads(memberId, FileType.POST, request.images)
            val postAttachments: List<PostAttachment> = files.map {
                attachmentService.save(
                    PostAttachment(post = post, file = it)
                )
            }
            post.attachments = postAttachments.toMutableList()
        }

        return post.toResponse()
    }

    override fun getList(memberId: String, groupId: String, type: PostTypeEnum, page: Int): List<PostResponse> {
        val member: Member = memberService.getByMemberId(memberId)
        val group: Group = groupService.getById(groupId)

        participantService.checkParticipant(member, group)

        return postService.getByGroupAndType(group, type, page).map { it.toResponse() }
    }

    override fun get(memberId: String, postId: String): PostResponse {
        val member: Member = memberService.getByMemberId(memberId)
        val post: Post = postService.getById(postId)

        participantService.checkParticipant(member, post.group)
        return post.toResponse()
    }

    @Transactional
    override fun update(memberId: String, postId: String, request: UpdatePostRequest): PostResponse {
        val writer: Member = memberService.getByMemberId(memberId)
        val post: Post = postService.getById(postId)

        if (writer != post.writer) {
            throw NotFoundPostAuthorityException()
        }
        return post.update(request).toResponse()
    }

    @Transactional
    override fun delete(memberId: String, postId: String): Boolean {
        val writer: Member = memberService.getByMemberId(memberId)
        val post: Post = postService.getById(postId)

        if (writer != post.writer) {
            throw NotFoundPostAuthorityException()
        }

        commentService.deleteByPost(post)
        postService.deleteById(postId)
        return true
    }
}