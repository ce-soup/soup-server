package com.github.soup.redis.group

interface RedisGroupRepository {
    fun getByKey(key: String): Int

    fun set(key: String, personnel: Int)

    fun addQueue(key: String, memberId: String)

    fun getQueue(key: String): Set<Any>?

    fun getOrder(key: String)

    fun deleteQueue(key: String, memberId: String)
}