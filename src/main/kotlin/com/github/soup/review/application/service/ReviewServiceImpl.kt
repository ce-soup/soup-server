package com.github.soup.review.application.service

import com.github.soup.review.domain.Review
import com.github.soup.review.exception.NotFoundReviewException
import com.github.soup.review.infra.persistence.ReviewRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository
) : ReviewService {

    @Transactional
    override fun save(review: Review): Review {
        return reviewRepository.save(review)
    }

    override fun getById(reviewId: String): Review {
        return reviewRepository.getById(reviewId).orElseThrow { NotFoundReviewException() }
    }

    override fun getByToId(toId: String, page: Int): List<Review> {
        val pageable: Pageable = PageRequest.of(page - 1, 10)
        return reviewRepository.getByToId(toId, pageable)
    }

    override fun getByFromIdAndToIdAndGroupId(fromId: String, toId: String, groupId: String): Review? {
        return reviewRepository.getByFromIdAndToIdAndGroupId(fromId, toId, groupId)
    }

    @Transactional
    override fun delete(review: Review) {
        reviewRepository.delete(review)
    }
}