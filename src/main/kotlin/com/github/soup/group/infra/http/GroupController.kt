package com.github.soup.group.infra.http

import com.github.soup.group.application.facade.GroupFacade
import com.github.soup.group.domain.GroupStatusEnum
import com.github.soup.group.domain.GroupTypeEnum
import com.github.soup.group.infra.http.request.CreateGroupRequest
import com.github.soup.group.infra.http.request.ListGroupRequest
import com.github.soup.group.infra.http.request.UpdateGroupRequest
import com.github.soup.group.infra.http.response.GroupResponse
import com.github.soup.member.infra.http.response.MemberResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/group")
class GroupController(
    private val groupFacade: GroupFacade
) {
    @Operation(summary = "그룹 생성")
    @PostMapping("/new")
    fun creatGroup(
        @Parameter(hidden = true) authentication: Authentication,
        @Valid request: CreateGroupRequest
    ): ResponseEntity<GroupResponse> =
        ResponseEntity.ok().body(
            groupFacade.create(
                authentication.name,
                request
            )
        )

    @Operation(summary = "그룹 조회")
    @GetMapping("/{groupId}")
    fun getGroup(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("groupId") groupId: String,
    ): ResponseEntity<GroupResponse> =
        ResponseEntity.ok().body(
            groupFacade.get(
                authentication.name,
                groupId
            )
        )

    @Operation(summary = "그룹 수정")
    @PutMapping("/{groupId}")
    fun updateGroup(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("groupId") groupId: String,
        @Valid request: UpdateGroupRequest
    ): ResponseEntity<GroupResponse> =
        ResponseEntity.ok().body(
            groupFacade.update(
                authentication.name,
                groupId,
                request
            )
        )

    @Operation(summary = "그룹 상태 시작")
    @PatchMapping("/{groupId}/start")
    fun startGroup(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("groupId") groupId: String,
    ): ResponseEntity<GroupResponse> =
        ResponseEntity.ok().body(
            groupFacade.start(
                authentication.name,
                groupId
            )
        )

    @Operation(summary = "그룹 상태 종료")
    @PatchMapping("/{groupId}/finish")
    fun finishGroup(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("groupId") groupId: String,
    ): ResponseEntity<GroupResponse> =
        ResponseEntity.ok().body(
            groupFacade.finish(
                authentication.name,
                groupId
            )
        )

    @Operation(summary = "모든 그룹 목록")
    @GetMapping("/list/{type}")
    fun allGroup(
        @PathVariable("type") type: GroupTypeEnum,
        @RequestParam(value = "page", required = false, defaultValue = "1") page: Int,
        @RequestParam(value = "status", required = false) status: GroupStatusEnum?,
        @RequestParam(value = "online", required = false) online: Boolean?,
        @RequestParam(value = "minPersonnel", required = false) minPersonnel: Int?,
        @RequestParam(value = "maxPersonnel", required = false) maxPersonnel: Int?,
    ): ResponseEntity<List<GroupResponse>> =
        ResponseEntity.ok().body(
            groupFacade.allGroups(
                ListGroupRequest(type, page, status, online, minPersonnel, maxPersonnel),
            )
        )

    @Operation(summary = "참여 그룹 목록")
    @GetMapping("/{memberId}/list/join")
    fun joinList(
        @PathVariable("memberId") memberId: String,
        @RequestParam(value = "page", required = false, defaultValue = "1") page: Int,
        @RequestParam(value = "status", required = true, defaultValue = "PROGRESS") status: GroupStatusEnum,
    ): ResponseEntity<List<GroupResponse>> {
        return ResponseEntity.ok().body(
            groupFacade.joinGroups(
                memberId = memberId,
                status = status,
                page = page
            )
        )
    }

    @Operation(summary = "참여자 목록")
    @GetMapping("/{groupId}/members")
    fun members(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("groupId") groupId: String
    ): ResponseEntity<List<MemberResponse>> {
        return ResponseEntity.ok().body(
            groupFacade.members(
                memberId = authentication.name,
                groupId = groupId
            )
        )
    }

    @Operation(summary = "인기 그룹 목록")
    @GetMapping("/popularity")
    fun popularity(): ResponseEntity<List<GroupResponse>> = ResponseEntity.ok().body(groupFacade.popularity())


    @Operation(summary = "참여 중인 인원 수 확인")
    @GetMapping("/{groupId}/count")
    fun getParticipantCount(
        @PathVariable("groupId") groupId: String
    ): ResponseEntity<Int> =
        ResponseEntity.ok().body(
            groupFacade.getParticipantCount(groupId)
        )
}
