package com.github.soup.search.application.facade

import com.github.soup.search.application.service.SearchService
import com.github.soup.search.infra.http.request.SearchType
import com.github.soup.search.infra.http.response.SearchResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class SearchFacadeImpl(
    private val searchService: SearchService
) : SearchFacade {

    override fun searchGroupAndUser(type: SearchType, page: Int, keyword: String): List<SearchResponse> {
        return when (type) {
            SearchType.GROUP -> {
                searchService.searchGroup(page, keyword).map {
                    SearchResponse.Group(it.toResponse())
                }
            }

            SearchType.MEMBER -> {
                searchService.searchUser(page, keyword).map {
                    SearchResponse.Member(it.toResponse())
                }
            }
        }
    }

}