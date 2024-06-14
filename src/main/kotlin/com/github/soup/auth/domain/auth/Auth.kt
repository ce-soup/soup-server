package com.github.soup.auth.domain.auth

import com.github.soup.auth.domain.role.Role
import com.github.soup.auth.domain.role.RoleType
import com.github.soup.common.domain.Core
import com.github.soup.member.domain.Member
import jakarta.persistence.*

@Entity
class Auth(
    @OneToOne(targetEntity = Member::class, fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "member_id")
    val member: Member,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: AuthType,

    @Column(nullable = false)
    val clientId: String,

    ) : Core() {
    @OneToMany(targetEntity = Role::class, mappedBy = "auth", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val roles: List<Role> = listOf(
        Role(
            auth = this,
            type = RoleType.General
        )
    )
}