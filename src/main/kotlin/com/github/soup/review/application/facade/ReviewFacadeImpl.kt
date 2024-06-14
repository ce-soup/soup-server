package com.github.soup.review.application.facade

import com.github.soup.group.application.service.GroupService
import com.github.soup.group.domain.Group
import com.github.soup.member.application.service.MemberService
import com.github.soup.member.domain.Member
import com.github.soup.review.application.service.ReviewService
import com.github.soup.review.domain.Review
import com.github.soup.review.exception.NotWriterException
import com.github.soup.review.exception.ReviewDontSeeSelfException
import com.github.soup.review.exception.ReviewDontWriteSelfException
import com.github.soup.review.infra.http.request.CreateReviewRequest
import com.github.soup.review.infra.http.request.UpdateReviewRequest
import com.github.soup.review.infra.http.response.ReviewResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class ReviewFacadeImpl(
    private val reviewService: ReviewService,
    private val memberService: MemberService,
    private val groupService: GroupService
) : ReviewFacade {

    @Transactional
    override fun create(memberId: String, request: CreateReviewRequest): ReviewResponse {
        if (memberId == request.targetId) {
            throw ReviewDontWriteSelfException()
        }
        val member: Member = memberService.getByMemberId(memberId)
        val target: Member = memberService.getByMemberId(request.targetId)
        val group: Group = groupService.getById(request.groupId)

        return reviewService.save(
            Review(
                from = member,
                to = target,
                group = group,
                content = request.content,
                score = request.score
            )
        ).toResponse()
    }

    @Transactional
    override fun update(memberId: String, reviewId: String, request: UpdateReviewRequest): ReviewResponse {
        val member: Member = memberService.getByMemberId(memberId)
        val review: Review = reviewService.getById(reviewId)

        if (!member.id.equals(review.from.id)) {
            throw NotWriterException()
        }

        return review.update(request.content, request.score).toResponse()
    }

    override fun getList(fromId: String, toId: String, page: Int): List<ReviewResponse> {
        if (fromId == toId) {
            throw ReviewDontSeeSelfException()
        }

        return reviewService.getByToId(toId, page).map { r -> r.toResponse() }
    }

    override fun get(memberId: String, reviewId: String): ReviewResponse {
        val review: Review = reviewService.getById(reviewId)

        if (memberId == review.to.id) {
            throw ReviewDontSeeSelfException()
        }

        return review.toResponse()
    }

    override fun check(memberId: String, groupId: String, toId: String): Boolean {
        return reviewService.getByFromIdAndToIdAndGroupId(memberId, toId, groupId) != null
    }

    @Transactional
    override fun delete(memberId: String, reviewId: String): Boolean {
        val member: Member = memberService.getByMemberId(memberId)
        val review: Review = reviewService.getById(reviewId)
        if (!member.id.equals(review.from.id)) {
            throw NotWriterException()
        }

        reviewService.delete(review)
        return true
    }
}