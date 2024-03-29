package com.github.soup.group.infra.persistence

import com.github.soup.file.domain.QFile.file
import com.github.soup.group.domain.Group
import com.github.soup.group.domain.GroupRepository
import com.github.soup.group.domain.GroupStatusEnum
import com.github.soup.group.domain.QGroup.group
import com.github.soup.group.infra.http.request.ListGroupRequest
import com.github.soup.member.domain.QMember.member
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class GroupRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val groupRepository: GroupJpaRepository
) : GroupRepository {

    override fun save(group: Group): Group {
        return groupRepository.save(group)
    }

    override fun getById(groupId: String): Optional<Group> {
        return groupRepository.findById(groupId)
    }

    override fun delete(group: Group) {
        groupRepository.delete(group)
    }

    override fun getList(condition: ListGroupRequest, pageable: Pageable): List<Group> {
        return queryFactory
            .selectFrom(group)
            .leftJoin(group.manager, member).fetchJoin()
            .leftJoin(group.image, file).fetchJoin()
            .where(
                group.type.eq(condition.type),
                stateEq(condition.status),
                isOnlineEq(condition.online),
                minPersonnelGoe(condition.minPersonnel),
                maxPersonnelLoe(condition.maxPersonnel)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(group.createdAt.desc())
            .fetch()
    }

    private fun stateEq(status: GroupStatusEnum?): BooleanExpression? {
        return if (status != null) group.status.eq(status)
        else null
    }

    private fun isOnlineEq(isOnline: Boolean?): BooleanExpression? {
        return if (isOnline != null) group.isOnline.eq(isOnline)
        else null
    }

    private fun minPersonnelGoe(minPersonnel: Int?): BooleanExpression? {
        return if (minPersonnel != null) group.personnel.goe(minPersonnel)
        else null
    }

    private fun maxPersonnelLoe(maxPersonnel: Int?): BooleanExpression? {
        return if (maxPersonnel != null) group.personnel.loe(maxPersonnel)
        else null
    }

    override fun searchGroup(name: String, pageable: Pageable): List<Group> {
        return groupRepository.findByNameContaining(name, pageable)
    }

    override fun getOrderByViewDecs(status: GroupStatusEnum): List<Group> {
        return groupRepository.findTop10ByStatusOrderByViewsDesc(status)
    }
}