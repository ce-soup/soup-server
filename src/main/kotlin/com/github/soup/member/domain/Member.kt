package com.github.soup.member.domain

import com.github.soup.common.domain.Core
import com.github.soup.file.domain.File
import com.github.soup.member.infra.http.response.MemberResponse
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Entity
class Member(
    @NotBlank
    @Column(nullable = false)
    var name: String,

    @NotBlank
    @Column(nullable = false)
    var nickname: String,

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val sex: SexType,

    @Column(nullable = true)
    var bio: String? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = true)
    var profileImage: File? = null,
) : Core() {
    fun toResponse(): MemberResponse {
        return MemberResponse(
            id = id!!,
            name = name,
            nickName = nickname,
            sex = sex,
            bio = bio,
            image = profileImage?.toResponse(),
            createdAt = createdAt!!,
            updatedAt = updatedAt!!,
        )
    }
}
