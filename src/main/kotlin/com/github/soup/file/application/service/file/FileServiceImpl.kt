package com.github.soup.file.application.service.file

import com.github.soup.file.domain.File
import com.github.soup.file.domain.FileRepository
import com.github.soup.file.domain.FileType
import com.github.soup.file.exception.NotSupportedFileFormatException
import com.github.soup.member.domain.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class FileServiceImpl(
    private val fileRepository: FileRepository,
) : FileService {

    private val FILE_TYPE: List<String> = listOf(
        "jpeg",
        "png",
        "jpg"
    )

    @Transactional
    override fun save(uploader: Member, type: FileType, image: MultipartFile): File {
        val mime = if (image.originalFilename == null) "jpeg" else
            image.originalFilename!!
                .substring(
                    image.originalFilename!!.lastIndexOf(".") + 1
                ).lowercase(Locale.getDefault())
        if (!FILE_TYPE.contains(mime)) {
            throw NotSupportedFileFormatException(mime)
        }

        return fileRepository.save(
            File(
                uploader = uploader,
                type = type,
                mime = mime
            )
        )
    }

    @Transactional
    override fun delete(fileId: String) {
        fileRepository.delete(fileId)
    }
}