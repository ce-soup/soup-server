package com.github.soup.redis.group

import com.github.soup.config.logger
import com.github.soup.file.application.service.storage.StorageServiceImpl
import jakarta.persistence.LockTimeoutException
import org.redisson.api.RedissonClient
import org.slf4j.Logger
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit


@Repository
class RedisGroupServiceImpl(
    private val redisTemplate: RedisTemplate<Any, Any>,
    private val redissonClient: RedissonClient
) : RedisGroupService {

    private final val log: Logger = logger<StorageServiceImpl>()

    // 매니저가 그룹 생성할 때 (key-value 형식의 NoSQL 사용)
    override fun getByKey(key: String): Int {
        val lock = redissonClient.getLock(key)

        try {
            val acquireLock = lock.tryLock(1, 2, TimeUnit.SECONDS)
            if (!acquireLock) {
                println("Lock 획득 실패")
                throw LockTimeoutException()
            }
            return redissonClient.getBucket<Int>(key).get()
        } catch (_: InterruptedException) {
        } finally {
            lock.unlock()
        }
        return -1
    }

    override fun set(key: String, personnel: Int) {
        redissonClient.getBucket<Int>(key).set(personnel)
    }

    // 팀원이 합류 요청할 때 (key, value, score 형식의 ZSET 사용)
    override fun addQueue(key: String, memberId: String) {
        val timeMillis = System.currentTimeMillis()
        redisTemplate.opsForZSet().add("group:$key", memberId, timeMillis.toDouble())
    }

    override fun getQueue(key: String): Set<Any>? {
        // 대기 요청 10개씩 조회
        return redisTemplate.opsForZSet().range("group:$key", 0, 9)
        // 대기 요청 1개씩 조회
        // return redisTemplate.opsForZSet().popMin(key)
    }

    override fun deleteQueue(key: String, memberId: String) {
        redisTemplate.opsForZSet().remove("group:$key", memberId)
    }

    override fun getOrder(key: String) {
        val queue = redisTemplate.opsForZSet().range(key, 0, -1)
        for (people in queue!!) {
            val rank = redisTemplate.opsForZSet().rank(key, people!!)
            log.info("'{}'님의 현재 대기열은 {}명 남았습니다.", people, rank)
        }
    }
}