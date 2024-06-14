package com.github.soup.scrap.domain

import com.github.soup.common.domain.Core
import com.github.soup.group.domain.Group
import com.github.soup.member.domain.Member
import com.github.soup.scrap.infra.http.response.ScrapResponse
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Scrap(

    @ManyToOne(targetEntity = Member::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member,

    @ManyToOne(targetEntity = Group::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    var group: Group

) : Core() {

    fun toResponse(): ScrapResponse {
        return ScrapResponse(
            id = id.toString(),
            member = member.toResponse(),
            group = group.toResponse(),
            createdAt = createdAt!!,
            updatedAt = updatedAt!!
        )
    }
}