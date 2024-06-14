package com.github.soup.post.comment.application.facade

import EmbeddedRedisConfig
import com.github.soup.auth.application.auth.AuthServiceImpl
import com.github.soup.auth.application.token.TokenServiceImpl
import com.github.soup.auth.domain.auth.AuthType
import com.github.soup.auth.infra.http.request.SignUpRequest
import com.github.soup.group.application.facade.GroupFacadeImpl
import com.github.soup.group.domain.GroupScopeEnum
import com.github.soup.group.domain.GroupTypeEnum
import com.github.soup.group.infra.http.request.CreateGroupRequest
import com.github.soup.member.domain.SexType
import com.github.soup.member.infra.persistence.MemberRepositoryImpl
import com.github.soup.post.appllication.facade.PostFacadeImpl
import com.github.soup.post.comment.exception.NotFoundCommentAuthorityException
import com.github.soup.post.comment.exception.NotFoundCommentException
import com.github.soup.post.comment.infra.http.request.CreateCommentRequest
import com.github.soup.post.comment.infra.http.response.CommentResponse
import com.github.soup.post.domain.PostTypeEnum
import com.github.soup.post.infra.http.request.CreatePostRequest
import jakarta.persistence.EntityManager
import kr.soupio.soup.group.entities.GroupRecruitmentEnum
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
@Transactional
@Import(value = [EmbeddedRedisConfig::class])
internal class CommentFacadeImplTest(
    @Autowired private val commentFacade: CommentFacadeImpl,
    @Autowired private val postFacade: PostFacadeImpl,
    @Autowired private val authService: AuthServiceImpl,
    @Autowired private val tokenService: TokenServiceImpl,
    @Autowired private val memberRepository: MemberRepositoryImpl,
    @Autowired private val groupFacade: GroupFacadeImpl,
    @Autowired private val em: EntityManager
) {

    @Test
    @DisplayName("댓글을 생성할 수 있어요.")
    fun create() {
        val response = authService.create(
            SignUpRequest(
                type = AuthType.KAKAO,
                token = UUID.randomUUID().toString(),
                name = "test_name",
                nickname = "test_nickname",
                sex = SexType.MALE
            )
        )

        val member = memberRepository.getById(tokenService.parse(response.accessToken))

        val group = groupFacade.create(
            memberId = member?.id!!,
            request = CreateGroupRequest(
                name = "test_group",
                content = "test_description",
                type = GroupTypeEnum.PROJECT,
                online = true, personnel = 3,
                scope = GroupScopeEnum.PUBLIC,
                recruitment = GroupRecruitmentEnum.FIRSTCOME,
                startHour = 13,
                startMinute = 30
            )
        )

        val post = postFacade.create(
            memberId = member.id!!,
            CreatePostRequest(
                groupId = group.id,
                type = PostTypeEnum.NOTICE,
                title = "title",
                content = "content",
                images = null
            )
        )

        val comment = commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = null,
                content = "content"
            )
        )

        Assertions.assertThat(comment).isInstanceOf(CommentResponse::class.java)
    }

    @Test
    @DisplayName("댓글과 대댓글을 조회할 수 있어요.")
    fun get() {
        val response = authService.create(
            SignUpRequest(
                type = AuthType.KAKAO,
                token = UUID.randomUUID().toString(),
                name = "test_name",
                nickname = "test_nickname",
                sex = SexType.MALE
            )
        )

        val member = memberRepository.getById(tokenService.parse(response.accessToken))

        val group = groupFacade.create(
            memberId = member?.id!!,
            request = CreateGroupRequest(
                name = "test_group",
                content = "test_description",
                type = GroupTypeEnum.PROJECT,
                online = true, personnel = 3,
                scope = GroupScopeEnum.PUBLIC,
                recruitment = GroupRecruitmentEnum.FIRSTCOME,
                startHour = 13,
                startMinute = 30
            )
        )

        val post = postFacade.create(
            memberId = member.id!!,
            CreatePostRequest(
                groupId = group.id,
                type = PostTypeEnum.NOTICE,
                title = "title",
                content = "content",
                images = null
            )
        )

        val comment = commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = null,
                content = "content"
            )
        )

        commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = comment.id,
                content = "content1"
            )
        )
        commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = comment.id,
                content = "content2"
            )
        )

        val comments = commentFacade.getByPostId(postId = post.id)

        Assertions.assertThat(comments.size).isEqualTo(1)
        Assertions.assertThat(comments[0].child?.size).isEqualTo(2)
    }

    @Test
    @DisplayName("댓글을 삭제할 수 있어요.")
    fun delete() {
        val response = authService.create(
            SignUpRequest(
                type = AuthType.KAKAO,
                token = UUID.randomUUID().toString(),
                name = "test_name",
                nickname = "test_nickname",
                sex = SexType.MALE
            )
        )

        val member = memberRepository.getById(tokenService.parse(response.accessToken))

        val group = groupFacade.create(
            memberId = member?.id!!,
            request = CreateGroupRequest(
                name = "test_group",
                content = "test_description",
                type = GroupTypeEnum.PROJECT,
                online = true, personnel = 3,
                scope = GroupScopeEnum.PUBLIC,
                recruitment = GroupRecruitmentEnum.FIRSTCOME,
                startHour = 13,
                startMinute = 30
            )
        )

        val post = postFacade.create(
            memberId = member.id!!,
            CreatePostRequest(
                groupId = group.id,
                type = PostTypeEnum.NOTICE,
                title = "title",
                content = "content",
                images = null
            )
        )

        val comment = commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = null,
                content = "content"
            )
        )

        commentFacade.delete(
            memberId = member.id!!,
            commentId = comment.id
        )

        assertThrows(NotFoundCommentException::class.java) {
            commentFacade.get(commentId = comment.id)
        }
    }

    @Test
    @DisplayName("댓글을 삭제하면 자동으로 대댓글도 삭제돼요.")
    fun deleteWithChild() {
        val response = authService.create(
            SignUpRequest(
                type = AuthType.KAKAO,
                token = UUID.randomUUID().toString(),
                name = "test_name",
                nickname = "test_nickname",
                sex = SexType.MALE
            )
        )

        val member = memberRepository.getById(tokenService.parse(response.accessToken))

        val group = groupFacade.create(
            memberId = member?.id!!,
            request = CreateGroupRequest(
                name = "test_group",
                content = "test_description",
                type = GroupTypeEnum.PROJECT,
                online = true, personnel = 3,
                scope = GroupScopeEnum.PUBLIC,
                recruitment = GroupRecruitmentEnum.FIRSTCOME,
                startHour = 13,
                startMinute = 30
            )
        )

        val post = postFacade.create(
            memberId = member.id!!,
            CreatePostRequest(
                groupId = group.id,
                type = PostTypeEnum.NOTICE,
                title = "title",
                content = "content",
                images = null
            )
        )

        val comment = commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = null,
                content = "content"
            )
        )

        commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = comment.id,
                content = "content"
            )
        )

        val childComment = commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = comment.id,
                content = "content"
            )
        )

        commentFacade.delete(
            memberId = member.id!!,
            commentId = comment.id
        )
        em.clear()

        assertThrows(NotFoundCommentException::class.java) {
            commentFacade.get(childComment.id)
        }
    }

    @Test
    @DisplayName("게시물을 삭제하면 자동으로 댓글도 삭제돼요.")
    fun deleteWithPost() {
        val response = authService.create(
            SignUpRequest(
                type = AuthType.KAKAO,
                token = UUID.randomUUID().toString(),
                name = "test_name",
                nickname = "test_nickname",
                sex = SexType.MALE
            )
        )

        val member = memberRepository.getById(tokenService.parse(response.accessToken))

        val group = groupFacade.create(
            memberId = member?.id!!,
            request = CreateGroupRequest(
                name = "test_group",
                content = "test_description",
                type = GroupTypeEnum.PROJECT,
                online = true, personnel = 3,
                scope = GroupScopeEnum.PUBLIC,
                recruitment = GroupRecruitmentEnum.FIRSTCOME,
                startHour = 13,
                startMinute = 30
            )
        )

        val post = postFacade.create(
            memberId = member.id!!,
            CreatePostRequest(
                groupId = group.id,
                type = PostTypeEnum.NOTICE,
                title = "title",
                content = "content",
                images = null
            )
        )

        val comment = commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = null,
                content = "content"
            )
        )

        commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = comment.id,
                content = "content"
            )
        )
        commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = comment.id,
                content = "content"
            )
        )

        postFacade.delete(
            memberId = member.id!!,
            postId = post.id
        )
        em.clear()

        assertThrows(NotFoundCommentException::class.java) {
            commentFacade.get(comment.id)
        }
    }

    @Test
    @DisplayName("댓글에 대한 권한이 없으면 댓글 삭제에 실패해요.")
    fun commentAuthorityFailed() {
        val response = authService.create(
            SignUpRequest(
                type = AuthType.KAKAO,
                token = UUID.randomUUID().toString(),
                name = "test_name",
                nickname = "test_nickname",
                sex = SexType.MALE
            )
        )
        val anotherResponse = authService.create(
            SignUpRequest(
                type = AuthType.KAKAO,
                token = UUID.randomUUID().toString(),
                name = "test_name",
                nickname = "test_nickname",
                sex = SexType.MALE
            )
        )

        val member = memberRepository.getById(tokenService.parse(response.accessToken))
        val anotherMember = memberRepository.getById(tokenService.parse(anotherResponse.accessToken))

        val group = groupFacade.create(
            memberId = member?.id!!,
            request = CreateGroupRequest(
                name = "test_group",
                content = "test_description",
                type = GroupTypeEnum.PROJECT,
                online = true, personnel = 3,
                scope = GroupScopeEnum.PUBLIC,
                recruitment = GroupRecruitmentEnum.FIRSTCOME,
                startHour = 13,
                startMinute = 30
            )
        )

        val post = postFacade.create(
            memberId = member.id!!,
            CreatePostRequest(
                groupId = group.id,
                type = PostTypeEnum.NOTICE,
                title = "title",
                content = "content",
                images = null
            )
        )

        val comment = commentFacade.create(
            memberId = member.id!!,
            CreateCommentRequest(
                postId = post.id,
                parentId = null,
                content = "content"
            )
        )

        assertThrows(NotFoundCommentAuthorityException::class.java) {
            commentFacade.delete(
                memberId = anotherMember?.id!!,
                commentId = comment.id
            )
        }
    }
}