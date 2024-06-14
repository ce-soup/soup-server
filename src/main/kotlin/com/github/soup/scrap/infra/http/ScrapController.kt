package com.github.soup.scrap.infra.http

import com.github.soup.scrap.application.facade.ScrapFacadeImpl
import com.github.soup.scrap.infra.http.request.CreateScrapRequest
import com.github.soup.scrap.infra.http.response.ScrapResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/scrap")
class ScrapController(
    private val scrapFacade: ScrapFacadeImpl
) {

    @Operation(summary = "스크랩하기")
    @PostMapping("/new")
    fun createScrap(
        @Parameter(hidden = true) authentication: Authentication,
        @RequestBody @Valid request: CreateScrapRequest
    ): ResponseEntity<ScrapResponse> =
        ResponseEntity.ok().body(
            scrapFacade.create(
                authentication.name,
                request
            )
        )

    @Operation(summary = "스크랩 목록 조회")
    @GetMapping("/{memberId}/list")
    fun getList(
        @PathVariable("memberId") memberId: String
    ): ResponseEntity<List<ScrapResponse>> =
        ResponseEntity.ok().body(
            scrapFacade.getList(
                memberId
            )
        )

    @Operation(summary = "스크랩 취소")
    @DeleteMapping("/{scrapId}")
    fun deleteScrap(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("scrapId") scrapId: String
    ): ResponseEntity<Boolean> =
        ResponseEntity.ok().body(
            scrapFacade.delete(
                authentication.name,
                scrapId
            )
        )
}