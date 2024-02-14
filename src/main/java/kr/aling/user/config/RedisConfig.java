package kr.aling.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 설정 class.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Configuration
public class RedisConfig {

    @Value("${aling.redis.host}")
    private String host;

    @Value("${aling.redis.port}")
    private int port;

    /**
     * RedisConnectionFactory를 LettuceConnectionFactory로 설정.
     *
     * @return LettuceConnectionFactory Bean
     * @author : 이수정
     * @since : 1.0
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    /**
     * Redis 상호작용 시 사용될 RedisTemplate Bean 설정.
     *
     * @return RedisTemplate Bean
     * @author : 이수정
     * @since : 1.0
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
