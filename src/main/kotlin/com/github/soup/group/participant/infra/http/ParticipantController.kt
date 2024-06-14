package com.github.soup.group.participant.infra.http

import com.github.soup.group.participant.application.facade.ParticipantFacade
import com.github.soup.group.participant.infra.http.request.AcceptParticipantRequest
import com.github.soup.group.participant.infra.http.request.CreateParticipantRequest
import com.github.soup.group.participant.infra.http.response.ParticipantResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/participant")
class ParticipantController(
    private val participantFacade: ParticipantFacade,
    private val redisTemplate: RedisTemplate<Any, Any>,
) {

    @Scheduled(fixedDelay = 1000)
    fun joinScheduler() {
        val scanOptions: ScanOptions = ScanOptions.scanOptions().match("group:*").build()
        redisTemplate.connectionFactory?.connection?.scan(scanOptions)
            ?.forEach {
                val key = it.toString().split(":")[1]
                participantFacade.firstcome(key)
            }
    }

    @Operation(summary = "참여 신청")
    @PostMapping("/new")
    fun createParticipant(
        @Parameter(hidden = true) authentication: Authentication,
        @RequestBody @Valid request: CreateParticipantRequest
    ): ResponseEntity<Boolean> =
        ResponseEntity.ok().body(
            participantFacade.join(
                authentication.name,
                request
            )
        )

    @Operation(summary = "참여 신청 목록")
    @GetMapping("/{groupId}")
    fun participantList(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("groupId") groupId: String,
    ): ResponseEntity<List<ParticipantResponse>> =
        ResponseEntity.ok().body(
            participantFacade.participantList(
                authentication.name,
                groupId
            )
        )

    @Operation(summary = "참여 신청 수락")
    @PatchMapping("/{groupId}/accept")
    fun accept(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("groupId") groupId: String,
        @RequestBody @Valid request: AcceptParticipantRequest
    ): ResponseEntity<Boolean> =
        ResponseEntity.ok().body(
            participantFacade.accept(
                authentication.name,
                groupId,
                request
            )
        )

    @Operation(summary = "참여 신청 여부 확인")
    @GetMapping("/check/{groupId}/register")
    fun checkRegister(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("groupId") groupId: String
    ): ResponseEntity<Boolean> =
        ResponseEntity.ok().body(
            participantFacade.isRegister(
                authentication.name,
                groupId
            )
        )

    @Operation(summary = "참여 수락 여부 확인")
    @GetMapping("/check/{groupId}/participant")
    fun checkParticipant(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("groupId") groupId: String
    ): ResponseEntity<Boolean> =
        ResponseEntity.ok().body(
            participantFacade.isParticipant(
                authentication.name,
                groupId
            )
        )
}