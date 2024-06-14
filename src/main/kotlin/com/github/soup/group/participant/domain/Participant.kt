package com.github.soup.group.participant.domain

import com.github.soup.common.domain.Core
import com.github.soup.group.domain.Group
import com.github.soup.member.domain.Member
import jakarta.persistence.*

@Entity
class Participant(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    var group: Group,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    @Column(nullable = false)
    var isAccepted: Boolean? = false,

    @Column(nullable = true)
    var message: String
) : Core()