package com.github.soup.group.participant.application.service

import com.github.soup.group.domain.Group
import com.github.soup.group.domain.GroupStatusEnum
import com.github.soup.group.participant.domain.Participant
import com.github.soup.group.participant.domain.ParticipantRepository
import com.github.soup.group.participant.infra.http.response.ParticipantResponse
import com.github.soup.member.domain.Member
import com.github.soup.member.infra.http.response.MemberResponse
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ParticipantServiceImpl(
    private val participantRepository: ParticipantRepository,
) : ParticipantService {

    @Transactional
    override fun save(group: Group, member: Member, isAccepted: Boolean, message: String): Boolean {
        participantRepository.save(
            Participant(
                group = group,
                member = member,
                isAccepted = isAccepted,
                message = message
            )
        )
        return true
    }

    override fun getParticipantList(group: Group): List<ParticipantResponse> {
        return participantRepository.getByGroupAndIsAccepted(
            group = group,
            isAccepted = false
        )
    }

    override fun getByMemberIdAndGroup(memberId: String, group: Group): Participant? {
        return participantRepository.getByMemberIdAndGroup(memberId, group)
    }

    override fun checkRegister(member: Member, group: Group): Boolean {
        return participantRepository.getByMemberAndGroupAndIsAccepted(
            member,
            group,
            null
        ) != null
    }

    override fun checkParticipant(member: Member, group: Group): Boolean {
        return participantRepository.getByMemberAndGroupAndIsAccepted(
            member,
            group,
            true
        ) != null
    }

    override fun joinGroupList(member: Member, status: GroupStatusEnum, page: Int): List<Group> {
        val pageable = PageRequest.of(page - 1, 10)
        return participantRepository.getJoinList(member = member, status = status, pageable = pageable)
    }

    override fun members(group: Group): List<MemberResponse> {
        return participantRepository.getByGroupAndIsAccepted(group = group, isAccepted = true).map { it.member }
    }

    override fun getParticipantCount(group: Group): Int {
        return participantRepository.getParticipantCount(group)
    }

}