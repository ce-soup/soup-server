package com.github.soup.post.infra.http

import com.github.soup.post.appllication.facade.PostFacadeImpl
import com.github.soup.post.domain.PostTypeEnum
import com.github.soup.post.infra.http.request.CreatePostRequest
import com.github.soup.post.infra.http.request.UpdatePostRequest
import com.github.soup.post.infra.http.response.PostResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/post")
class PostController(
    private val postFacade: PostFacadeImpl
) {
    @Operation(summary = "게시글 생성")
    @PostMapping("/new")
    fun createPost(
        @Parameter(hidden = true) authentication: Authentication,
        @Valid request: CreatePostRequest
    ): ResponseEntity<PostResponse> =
        ResponseEntity.ok().body(
            postFacade.create(
                authentication.name,
                request
            )
        )

    @Operation(summary = "게시글 타입별 조회")
    @GetMapping("/{groupId}/{type}")
    fun getPosts(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("groupId") groupId: String,
        @PathVariable("type") type: PostTypeEnum,
        @RequestParam(value = "page", required = false, defaultValue = "1") page: Int,
    ): ResponseEntity<List<PostResponse>> =
        ResponseEntity.ok().body(
            postFacade.getList(
                authentication.name,
                groupId,
                type,
                page
            )
        )

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{postId}")
    fun getPost(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("postId") postId: String,
    ): ResponseEntity<PostResponse> =
        ResponseEntity.ok().body(
            postFacade.get(
                authentication.name,
                postId
            )
        )

    @Operation(summary = "게시글 수정")
    @PatchMapping("/{postId}")
    fun updatePost(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("postId") postId: String,
        @Valid request: UpdatePostRequest
    ): ResponseEntity<PostResponse> =
        ResponseEntity.ok().body(
            postFacade.update(
                authentication.name,
                postId,
                request
            )
        )

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    fun deletePost(
        @Parameter(hidden = true) authentication: Authentication,
        @PathVariable("postId") postId: String,
    ): ResponseEntity<Boolean> =
        ResponseEntity.ok().body(
            postFacade.delete(
                authentication.name,
                postId
            )
        )
}