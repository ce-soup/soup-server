package com.github.soup.group.application.service

import com.github.soup.group.domain.Group
import com.github.soup.group.domain.GroupRepository
import com.github.soup.group.domain.GroupStatusEnum
import com.github.soup.group.exception.NotFoundGroupException
import com.github.soup.group.infra.http.request.ListGroupRequest
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GroupServiceImpl(
    private val groupRepository: GroupRepository
) : GroupService {

    @Transactional
    override fun save(group: Group): Group {
        return groupRepository.save(group)
    }

    override fun getById(groupId: String): Group {
        return groupRepository.getById(groupId).orElseThrow { NotFoundGroupException() }
    }

    @Transactional
    override fun delete(group: Group) {
        return groupRepository.delete(group)
    }

    override fun allGroupList(request: ListGroupRequest): List<Group> {
        val pageable = PageRequest.of(request.page - 1, 10)
        return groupRepository.getList(
            condition = request,
            pageable = pageable
        )
    }

    override fun getViewDecs(): List<Group> {
        return groupRepository.getOrderByViewDecs(GroupStatusEnum.READY)
    }

}