package com.github.soup.follow.application.service

import com.github.soup.follow.domain.Follow
import com.github.soup.follow.exception.NotFoundFollowException
import com.github.soup.follow.infra.persistence.FollowRepository
import com.github.soup.member.domain.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FollowServiceImpl(
    private val followRepository: FollowRepository
) : FollowService {

    @Transactional
    override fun save(follow: Follow): Follow {
        return followRepository.save(follow)
    }

    override fun getFromList(member: Member): List<Follow> {
        return followRepository.getByFrom(member)
    }

    override fun getToList(member: Member): List<Follow> {
        return followRepository.getByTo(member)
    }

    override fun getByFromIdAndToId(fromId: String, toId: String): Follow? {
        return followRepository.getByFromIdAndToId(fromId, toId)
    }

    override fun getByMemberAndTo(member: Member, followId: String): Follow {
        return followRepository.getByMemberAndTo(member, followId) ?: throw NotFoundFollowException()
    }

    @Transactional
    override fun delete(follow: Follow) {
        followRepository.delete(follow)
    }
}