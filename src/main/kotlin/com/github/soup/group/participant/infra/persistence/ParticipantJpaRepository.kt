package com.github.soup.group.participant.infra.persistence

import com.github.soup.group.domain.Group
import com.github.soup.group.participant.domain.Participant
import com.github.soup.member.domain.Member
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantJpaRepository : JpaRepository<Participant, String> {
    fun findByMemberAndGroupAndIsAccepted(member: Member, group: Group, isAccepted: Boolean): Participant?

    fun findByGroupAndIsAccepted(group: Group, isAccepted: Boolean, pageable: Pageable): List<Participant>
}