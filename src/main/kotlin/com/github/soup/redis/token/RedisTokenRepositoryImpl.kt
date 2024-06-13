package com.github.soup.redis.token

import com.github.soup.auth.domain.token.Token
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisTokenRepositoryImpl(
        private val redisTemplate: RedisTemplate<Any, Any>
) : RedisTokenRepository {
    override fun getByKey(key: String): Token? {
        return getOperations().get(key) as Token?
    }

    override fun save(key: String, token: Token, timeToLive: Long): Token {
        getOperations().set(key, token, Duration.ofMillis(timeToLive))
        return token
    }

    override fun delete(key: String) {
        getOperations().getAndDelete(key)
    }

    private fun getOperations(): ValueOperations<Any, Any> {
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(Token::class.java)
        return redisTemplate.opsForValue()
    }
}