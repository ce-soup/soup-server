package com.github.soup.group.application.facade

import com.github.soup.file.application.facade.FileFacadeImpl
import com.github.soup.file.domain.File
import com.github.soup.file.domain.FileType
import com.github.soup.group.application.service.GroupServiceImpl
import com.github.soup.group.domain.Group
import com.github.soup.group.domain.GroupStatusEnum
import com.github.soup.group.exception.NotFoundManagerAuthorityException
import com.github.soup.group.infra.http.request.CreateGroupRequest
import com.github.soup.group.infra.http.request.ListGroupRequest
import com.github.soup.group.infra.http.request.UpdateGroupRequest
import com.github.soup.group.infra.http.response.GroupResponse
import com.github.soup.group.participant.application.service.ParticipantServiceImpl
import com.github.soup.member.application.service.MemberServiceImpl
import com.github.soup.member.domain.Member
import com.github.soup.member.infra.http.response.MemberResponse
import com.github.soup.redis.group.RedisGroupRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class GroupFacadeImpl(
    private val groupService: GroupServiceImpl,
    private val memberService: MemberServiceImpl,
    private val fileFacade: FileFacadeImpl,
    private val participantService: ParticipantServiceImpl,
    private val redisGroupRepository: RedisGroupRepository,
) : GroupFacade {

    @Transactional
    override fun create(memberId: String, request: CreateGroupRequest): GroupResponse {
        val manager: Member = memberService.getByMemberId(memberId)

        val file: File? = request.image?.let {
            fileFacade.upload(memberId = memberId, type = FileType.GROUP, image = it)
        }
        val group = groupService.save(
            request.toEntity(manager = manager, image = file)
        )

        participantService.save(group = group, member = manager, isAccepted = true, message = "")

        redisGroupRepository.save(group.id!!, group.personnel - 1)
        return group.toResponse()
    }

    @Transactional
    override fun update(memberId: String, groupId: String, request: UpdateGroupRequest): GroupResponse {
        val member: Member = memberService.getByMemberId(memberId)
        val group: Group = groupService.getById(groupId)
        if (member != group.manager)
            throw NotFoundManagerAuthorityException()

        return group.update(request = request).toResponse()
    }

    @Transactional
    override fun get(memberId: String, groupId: String): GroupResponse {
        val group = groupService.getById(groupId)
        group.views += 1
        return group.toResponse()
    }

    @Transactional
    override fun start(memberId: String, groupId: String): GroupResponse {
        val member: Member = memberService.getByMemberId(memberId)
        val group: Group = groupService.getById(groupId)
        if (member != group.manager) {
            throw NotFoundManagerAuthorityException()
        }
        group.status = GroupStatusEnum.PROGRESS
        return group.toResponse()
    }

    @Transactional
    override fun finish(memberId: String, groupId: String): GroupResponse {
        val member: Member = memberService.getByMemberId(memberId)
        val group: Group = groupService.getById(groupId)
        if (member != group.manager) {
            throw NotFoundManagerAuthorityException()
        }
        group.status = GroupStatusEnum.FINISH
        return group.toResponse()
    }

    override fun allGroups(request: ListGroupRequest): List<GroupResponse> {
        return groupService.allGroupList(request).map { it.toResponse() }
    }

    override fun joinGroups(memberId: String, status: GroupStatusEnum, page: Int): List<GroupResponse> {
        val member = memberService.getByMemberId(memberId)
        return participantService.joinGroupList(member = member, status = status, page = page).map { it.toResponse() }
    }

    override fun members(memberId: String, groupId: String): List<MemberResponse> {
        val member = memberService.getByMemberId(memberId)
        val group: Group = groupService.getById(groupId)
        participantService.checkParticipant(member = member, group = group)
        return participantService.members(group = group)
    }

    override fun popularity(): List<GroupResponse> {
        return groupService.getViewDecs().map { it.toResponse() }
    }

    override fun getParticipantCount(groupId: String): Int {
        val group: Group = groupService.getById(groupId)
        return participantService.getParticipantCount(group)
    }
}