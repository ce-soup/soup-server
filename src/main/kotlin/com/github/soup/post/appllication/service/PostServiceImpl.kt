package com.github.soup.post.appllication.service

import com.github.soup.post.domain.Post
import com.github.soup.post.exception.NotFoundPostException
import com.github.soup.post.infra.persistence.PostRepositoryImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostServiceImpl(
    private val postRepository: PostRepositoryImpl
) : PostService {

    @Transactional
    override fun save(post: Post): Post {
        return postRepository.save(post)
    }

    override fun getById(postId: String): Post {
        return postRepository.getById(postId).orElseThrow { NotFoundPostException() }
    }

    @Transactional
    override fun deleteById(postId: String) {
        return postRepository.deleteById(postId)
    }
}