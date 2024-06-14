package com.github.soup.scrap.application.facade

import com.github.soup.group.application.service.GroupService
import com.github.soup.group.domain.Group
import com.github.soup.member.application.service.MemberService
import com.github.soup.member.domain.Member
import com.github.soup.scrap.application.service.ScrapService
import com.github.soup.scrap.domain.Scrap
import com.github.soup.scrap.exception.NotFoundScrapAuthorityException
import com.github.soup.scrap.infra.http.request.CreateScrapRequest
import com.github.soup.scrap.infra.http.response.ScrapResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class ScrapFacadeImpl(
    private val scrapService: ScrapService,
    private val memberService: MemberService,
    private val groupService: GroupService
) : ScrapFacade {

    @Transactional
    override fun create(memberId: String, request: CreateScrapRequest): ScrapResponse {
        val member: Member = memberService.getByMemberId(memberId)
        val group: Group = groupService.getById(request.groupId)

        val scrap = scrapService.getByMemberAndGroup(member, group)
        if (scrap != null) {
            return scrap.toResponse()
        }
        return scrapService.save(
            Scrap(
                member = member,
                group = group
            )
        ).toResponse()

    }

    override fun getList(memberId: String): List<ScrapResponse> {
        val member: Member = memberService.getByMemberId(memberId)
        return scrapService.getByMember(member).map { s -> s.toResponse() }
    }

    @Transactional
    override fun delete(memberId: String, scrapId: String): Boolean {
        val member: Member = memberService.getByMemberId(memberId)
        val scrap: Scrap = scrapService.getById(scrapId)

        if (member.id != scrap.member.id) {
            throw NotFoundScrapAuthorityException()
        }

        scrapService.delete(scrap)
        return true
    }
}