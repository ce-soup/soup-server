package com.github.soup.search.application.service

import com.github.soup.group.domain.Group
import com.github.soup.group.domain.GroupRepository
import com.github.soup.member.domain.Member
import com.github.soup.member.domain.MemberRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SearchServiceImpl(
    private val groupRepository: GroupRepository,
    private val memberRepository: MemberRepository,
) : SearchService {

    override fun searchGroup(page: Int, keyword: String): List<Group> {
        val pageable: Pageable = PageRequest.of(page - 1, 10)
        return groupRepository.searchGroup(
            name = keyword,
            pageable = pageable
        )
    }

    override fun searchUser(page: Int, keyword: String): List<Member> {
        val pageable: Pageable = PageRequest.of(page - 1, 10)
        return memberRepository.searchMember(
            name = keyword,
            pageable = pageable
        )
    }
}
