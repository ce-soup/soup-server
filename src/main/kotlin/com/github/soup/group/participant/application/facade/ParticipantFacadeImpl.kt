package com.github.soup.group.participant.application.facade

import com.github.soup.group.application.service.GroupServiceImpl
import com.github.soup.group.domain.Group
import com.github.soup.group.exception.NotFoundManagerAuthorityException
import com.github.soup.group.participant.application.service.ParticipantServiceImpl
import com.github.soup.group.participant.exception.ExceededPersonnelException
import com.github.soup.group.participant.infra.http.request.AcceptParticipantRequest
import com.github.soup.group.participant.infra.http.request.CreateParticipantRequest
import com.github.soup.group.participant.infra.http.response.ParticipantResponse
import com.github.soup.member.application.service.MemberServiceImpl
import com.github.soup.member.domain.Member
import kr.soupio.soup.group.entities.GroupRecruitmentEnum
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class ParticipantFacadeImpl(
	private val participantService: ParticipantServiceImpl,
	private val memberService: MemberServiceImpl,
	private val groupService: GroupServiceImpl
) : ParticipantFacade {

	@Transactional
	override fun join(memberId: String, request: CreateParticipantRequest): Boolean {
		val member: Member = memberService.getByMemberId(memberId)
		val group: Group = groupService.getById(request.groupId)

		if (group.recruitment == GroupRecruitmentEnum.FIRSTCOME) {
			if(group.personnel<participantService.getParticipantCount(group)){
				throw ExceededPersonnelException()
			}

			participantService.save(
				group = group,
				member = member,
				isAccepted = true,
				message = request.message
			)
		}
		if (group.recruitment == GroupRecruitmentEnum.SELECTION) {
			participantService.save(
				group = group,
				member = member,
				isAccepted = false,
				message = request.message
			)
		}
		return true
	}

	override fun participantList(memberId: String, groupId: String): List<ParticipantResponse> {
		val member: Member = memberService.getByMemberId(memberId)
		val group: Group = groupService.getById(groupId)

		if (!member.id.equals(group.manager.id)) {
			throw NotFoundManagerAuthorityException()
		}
		return participantService.getParticipantList(group)
	}

	@Transactional
	override fun accept(memberId: String, groupId: String, request: AcceptParticipantRequest): Boolean {
		val member: Member = memberService.getByMemberId(memberId)
		val group: Group = groupService.getById(groupId)

		if (!member.id.equals(group.manager.id)) {
			throw NotFoundManagerAuthorityException()
		}

		if(group.personnel<=request.memberIdList.size){
			throw ExceededPersonnelException()
		}

		request.memberIdList.forEach { participantService.getByMemberIdAndGroup(it, group)?.isAccepted = true }
		return true
	}

	override fun isRegister(memberId: String, groupId: String): Boolean {
		return participantService.checkRegister(
			member = memberService.getByMemberId(memberId),
			group = groupService.getById(groupId)
		)
	}

	override fun isParticipant(memberId: String, groupId: String): Boolean {
		return participantService.checkParticipant(
			member = memberService.getByMemberId(memberId),
			group = groupService.getById(groupId)
		)
	}
}