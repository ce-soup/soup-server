import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer

@TestConfiguration
class EmbeddedRedisConfig(
    @Value("\${spring.redis.port}")
    private val port: Int,

    @Value("\${spring.redis.max-memory}")
    private val maxMemory: String
) {

    private lateinit var redisServer: RedisServer

    @PostConstruct
    fun postConstruct() {
        redisServer = RedisServer.builder()
            .port(port)
            .setting("maxmemory $maxMemory")
            .build()
        redisServer.start()
    }

    @PreDestroy
    fun preDestroy() {
        redisServer.stop()
    }
}