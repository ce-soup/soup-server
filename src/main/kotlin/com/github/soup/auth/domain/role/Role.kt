package com.github.soup.auth.domain.role

import com.github.soup.auth.domain.auth.Auth
import com.github.soup.common.domain.Core
import jakarta.persistence.*

@Entity
class Role(
    @ManyToOne(targetEntity = Auth::class, fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "auth_id")
    val auth: Auth,

    @Enumerated(EnumType.STRING)
    val type: RoleType
) : Core()