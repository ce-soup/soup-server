package com.github.soup.group.participant.domain

import com.github.soup.group.domain.Group
import com.github.soup.group.domain.GroupStatusEnum
import com.github.soup.member.domain.Member
import org.springframework.data.domain.Pageable

interface ParticipantRepository {

    fun save(participant: Participant): Participant

    fun participant(member: Member, group: Group): Participant?

    fun getJoinList(member: Member, status: GroupStatusEnum, pageable: Pageable): List<Group>

    fun getMembers(group: Group, pageable: Pageable): List<Participant>
}