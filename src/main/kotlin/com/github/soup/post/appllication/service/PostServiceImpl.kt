package com.github.soup.post.appllication.service

import com.github.soup.group.domain.Group
import com.github.soup.post.domain.Post
import com.github.soup.post.domain.PostTypeEnum
import com.github.soup.post.exception.NotFoundPostException
import com.github.soup.post.infra.persistence.PostRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostServiceImpl(
    private val postRepository: PostRepository
) : PostService {

    @Transactional
    override fun save(post: Post): Post {
        return postRepository.save(post)
    }

    override fun getByGroupAndType(group: Group, type: PostTypeEnum, page: Int): List<Post> {
        return postRepository.getListByType(
            group,
            type,
            PageRequest.of(page - 1, 10)
        )
    }

    override fun getById(postId: String): Post {
        return postRepository.getById(postId).orElseThrow { NotFoundPostException() }
    }

    @Transactional
    override fun deleteById(postId: String) {
        return postRepository.deleteById(postId)
    }
}