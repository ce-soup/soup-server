package com.github.soup.group.application.facade

import com.github.soup.group.domain.GroupStatusEnum
import com.github.soup.group.infra.http.request.CreateGroupRequest
import com.github.soup.group.infra.http.request.ListGroupRequest
import com.github.soup.group.infra.http.request.UpdateGroupRequest
import com.github.soup.group.infra.http.response.GroupResponse
import com.github.soup.member.infra.http.response.MemberResponse

interface GroupFacade {

    fun create(memberId: String, request: CreateGroupRequest): GroupResponse

    fun update(memberId: String, groupId: String, request: UpdateGroupRequest): GroupResponse

    fun get(memberId: String, groupId: String): GroupResponse

    fun start(memberId: String, groupId: String): GroupResponse

    fun finish(memberId: String, groupId: String): GroupResponse

    fun allGroups(request: ListGroupRequest): List<GroupResponse>

    fun joinGroups(memberId: String, status: GroupStatusEnum, page: Int): List<GroupResponse>

    fun members(memberId: String, groupId: String): List<MemberResponse>

    fun popularity(): List<GroupResponse>

    fun getParticipantCount(groupId: String): Int
}
