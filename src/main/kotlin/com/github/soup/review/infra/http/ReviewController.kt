package com.github.soup.review.infra.http

import com.github.soup.review.application.facade.ReviewFacade
import com.github.soup.review.infra.http.request.CreateReviewRequest
import com.github.soup.review.infra.http.request.UpdateReviewRequest
import com.github.soup.review.infra.http.response.ReviewResponse
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/review")
class ReviewController(
    private val reviewFacade: ReviewFacade
) {

    @PostMapping("/new")
    fun createReview(
        @Parameter(hidden = true) authentication: Authentication,
        @RequestBody @Valid request: CreateReviewRequest
    ): ResponseEntity<ReviewResponse> =
        ResponseEntity.ok().body(
            reviewFacade.create(
                authentication.name,
                request
            )
        )

    @PatchMapping("/{reviewId}")
    fun updateReview(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("reviewId") reviewId: String,
        @RequestBody @Valid request: UpdateReviewRequest
    ): ResponseEntity<ReviewResponse> =
        ResponseEntity.ok().body(
            reviewFacade.update(
                authentication.name,
                reviewId,
                request
            )
        )

    @GetMapping("/list/{toId}")
    fun getList(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("toId") toId: String,
        @RequestParam(value = "page", required = false, defaultValue = "1") page: Int,
    ): ResponseEntity<List<ReviewResponse>> =
        ResponseEntity.ok().body(
            reviewFacade.getList(
                authentication.name,
                toId,
                page
            )
        )

    @GetMapping("/{reviewId}")
    fun get(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("reviewId") reviewId: String
    ): ResponseEntity<ReviewResponse> =
        ResponseEntity.ok().body(
            reviewFacade.get(
                authentication.name,
                reviewId
            )
        )

    @GetMapping("/check/{groupId}/{toId}")
    fun check(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("groupId") groupId: String,
        @PathVariable("toId") toId: String
    ): ResponseEntity<Boolean> =
        ResponseEntity.ok().body(
            reviewFacade.check(
                authentication.name,
                groupId,
                toId
            )
        )

    @DeleteMapping("/{reviewId}")
    fun deleteScrap(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("reviewId") reviewId: String
    ): ResponseEntity<Boolean> =
        ResponseEntity.ok().body(
            reviewFacade.delete(
                authentication.name,
                reviewId
            )
        )
}