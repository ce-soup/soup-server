package com.github.soup.follow.infra.http

import com.github.soup.follow.application.facade.FollowFacade
import com.github.soup.follow.infra.http.response.FollowResponse
import com.github.soup.member.infra.http.response.MemberResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/follow")
class FollowController(
    private val followFacade: FollowFacade
) {

    @PostMapping("/new/{targetId}")
    fun createFollow(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("targetId") targetId: String
    ): ResponseEntity<FollowResponse> =
        ResponseEntity.ok().body(
            followFacade.create(
                authentication.name,
                targetId
            )
        )

    @Operation(summary = "팔로잉 목록 조회")
    @GetMapping("/following/{memberId}")
    fun getFromList(
        @PathVariable("memberId") memberId: String
    ): ResponseEntity<List<MemberResponse>> =
        ResponseEntity.ok().body(
            followFacade.getFollowingList(
                memberId,
            )
        )

    @Operation(summary = "팔로워 목록 조회")
    @GetMapping("/follower/{memberId}")
    fun getToList(
        @PathVariable("memberId") memberId: String,
    ): ResponseEntity<List<MemberResponse>> =
        ResponseEntity.ok().body(
            followFacade.getFollowerList(
                memberId,
            )
        )


    @DeleteMapping("/{targetId}")
    fun deleteFollow(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("targetId") targetId: String
    ): ResponseEntity<Boolean> =
        ResponseEntity.ok().body(
            followFacade.delete(
                authentication.name,
                targetId
            )
        )
}