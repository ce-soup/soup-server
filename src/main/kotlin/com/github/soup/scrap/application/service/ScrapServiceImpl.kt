package com.github.soup.scrap.application.service

import com.github.soup.group.domain.Group
import com.github.soup.member.domain.Member
import com.github.soup.scrap.domain.Scrap
import com.github.soup.scrap.exception.NotFoundScrapException
import com.github.soup.scrap.infra.persistence.ScrapRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ScrapServiceImpl(
    private val scrapRepository: ScrapRepository
) : ScrapService {

    @Transactional
    override fun save(scrap: Scrap): Scrap {
        return scrapRepository.save(scrap)
    }

    override fun getById(scrapId: String): Scrap {
        return scrapRepository.getById(scrapId).orElseThrow { NotFoundScrapException() }
    }

    override fun getByMember(member: Member): List<Scrap> {
        return scrapRepository.getByMember(member)
    }

    override fun getByMemberAndGroup(member: Member, group: Group): Scrap? {
        return scrapRepository.getByMemberAndGroup(member, group).orElse(null)
    }

    @Transactional
    override fun delete(scrap: Scrap) {
        return scrapRepository.delete(scrap)
    }
}