package com.github.soup.redis.group

interface RedisGroupRepository {
    fun getByKey(key: String): Int

    fun save(key: String, personnel: Int)

    fun decrease(key: String)

    fun delete(key: String)

    fun addQueue(key: String, memberId: String)

    fun getQueue(key: String): Set<Any>?

    fun getOrder(key: String)

    fun deleteQueue(key: String, memberId: String)
}