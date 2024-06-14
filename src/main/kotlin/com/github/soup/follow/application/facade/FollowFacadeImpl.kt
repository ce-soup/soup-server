package com.github.soup.follow.application.facade

import com.github.soup.follow.application.service.FollowService
import com.github.soup.follow.domain.Follow
import com.github.soup.follow.exception.NotFollowSelfException
import com.github.soup.follow.exception.NotFoundFollowAuthorityException
import com.github.soup.follow.infra.http.response.FollowResponse
import com.github.soup.member.application.service.MemberService
import com.github.soup.member.domain.Member
import com.github.soup.member.infra.http.response.MemberResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class FollowFacadeImpl(
    private val followService: FollowService,
    private val memberService: MemberService,
) : FollowFacade {

    @Transactional
    override fun create(fromId: String, targetId: String): FollowResponse {
        if (fromId == targetId) {
            throw NotFollowSelfException()
        }

        val follow: Follow? = followService.getByFromIdAndToId(fromId, targetId)
        if (follow != null) {
            return follow.toResponse()
        }

        return followService.save(
            Follow(
                from = memberService.getByMemberId(fromId),
                to = memberService.getByMemberId(targetId)
            )
        ).toResponse()
    }

    override fun getFollowingList(memberId: String): List<MemberResponse> {
        val from: Member = memberService.getByMemberId(memberId)
        return followService.getFromList(from).map { f -> f.to.toResponse() }
    }

    override fun getFollowerList(memberId: String): List<MemberResponse> {
        val to: Member = memberService.getByMemberId(memberId)
        return followService.getToList(to).map { f -> f.from.toResponse() }
    }

    @Transactional
    override fun delete(memberId: String, followId: String): Boolean {
        val member: Member = memberService.getByMemberId(memberId)
        val follow: Follow = followService.getByMemberAndTo(member, followId)

        if (!member.id.equals(follow.from.id)) {
            throw NotFoundFollowAuthorityException()
        }
        followService.delete(follow)
        return true
    }

}