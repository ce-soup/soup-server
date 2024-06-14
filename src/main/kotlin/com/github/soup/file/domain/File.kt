package com.github.soup.file.domain

import com.github.soup.common.domain.Core
import com.github.soup.file.infra.http.response.FileResponse
import com.github.soup.member.domain.Member
import jakarta.persistence.*
import java.util.*


@Entity
class File(
    @ManyToOne(targetEntity = Member::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id")
    var uploader: Member,

    @Column(name = "file_key", nullable = false)
    var key: String
) : Core() {

    constructor(uploader: Member, type: FileType, mime: String) : this(
        uploader = uploader,
        key = "$type/${UUID.randomUUID()}.$mime",
    )

    fun toResponse(): FileResponse {
        return FileResponse(
            id!!,
            createdAt!!,
            updatedAt!!,
            "http://133.186.215.107:9000/bucket/$key"
        )
    }
}