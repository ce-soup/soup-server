package com.github.soup.search.infra.http

import com.github.soup.search.application.facade.SearchFacade
import com.github.soup.search.infra.http.request.SearchType
import com.github.soup.search.infra.http.response.SearchResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/search")
class SearchController(
    private val searchFacade: SearchFacade
) {
    @GetMapping("/{type}")
    fun search(
        @PathVariable("type") type: SearchType,
        @RequestParam(value = "page", required = false, defaultValue = "1") page: Int,
        @RequestParam(value = "keyword", required = true) keyword: String
    ): ResponseEntity<List<SearchResponse>> {
        return ResponseEntity.ok().body(
            searchFacade.searchGroupAndUser(type, page, keyword)
        )
    }
}