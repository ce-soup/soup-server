package com.github.soup.file.application.facade

import com.github.soup.file.application.service.file.FileService
import com.github.soup.file.application.service.storage.StorageService
import com.github.soup.file.domain.File
import com.github.soup.file.domain.FileType
import com.github.soup.file.exception.StorageUploadException
import com.github.soup.member.application.service.MemberService
import com.github.soup.member.domain.Member
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Component
@Transactional
class FileFacadeImpl(
    private val fileService: FileService,
    private val storageService: StorageService,
    private val memberService: MemberService
) : FileFacade {

    override fun upload(memberId: String, type: FileType, image: MultipartFile): File {
        val member: Member = memberService.getByMemberId(memberId)
        val file: File = fileService.save(
            uploader = member,
            type = type,
            image = image
        )

        if (!storageService.upload(key = file.key, image = image)) {
            throw StorageUploadException()
        }

        return file
    }

    override fun uploads(memberId: String, type: FileType, images: List<MultipartFile>): List<File> {
        val member: Member = memberService.getByMemberId(memberId)

        return images.map { image ->
            val file: File = fileService.save(member, type, image)
            storageService.upload(key = file.key, image = image)
            file
        }
    }

}