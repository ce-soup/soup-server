package com.github.soup.redis.token

import com.github.soup.auth.domain.token.Token

interface RedisTokenRepository {
    fun getByKey(key: String): Token?

    fun save(key: String, token: Token, timeToLive: Long): Token

    fun delete(key: String)
}