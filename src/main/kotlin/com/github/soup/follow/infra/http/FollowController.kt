package com.github.soup.follow.infra.http

import com.github.soup.follow.application.facade.FollowFacadeImpl
import com.github.soup.follow.infra.http.response.FollowResponse
import com.github.soup.member.infra.http.response.MemberResponse
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/api/follow")
class FollowController(
	private val followFacade: FollowFacadeImpl
) {

	@PostMapping("/new/{targetId}")
	fun createFollow(
		@ApiIgnore authentication: Authentication,
		@PathVariable("targetId") targetId: String
	): ResponseEntity<FollowResponse> =
		ResponseEntity.ok().body(
			followFacade.create(
				authentication.name,
				targetId
			)
		)

	@ApiOperation(value = "팔로잉 목록 조회")
	@GetMapping("/following/{memberId}")
	fun getFromList(
		@PathVariable("memberId") memberId: String
	): ResponseEntity<List<MemberResponse>> =
		ResponseEntity.ok().body(
			followFacade.getFollowingList(
				memberId,
			)
		)

	@ApiOperation(value = "팔로워 목록 조회")
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
		@ApiIgnore authentication: Authentication,
		@PathVariable("targetId") targetId: String
	): ResponseEntity<Boolean> =
		ResponseEntity.ok().body(
			followFacade.delete(
				authentication.name,
				targetId
			)
		)
}