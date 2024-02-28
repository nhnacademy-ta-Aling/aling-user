package kr.aling.user.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis 설정 Properties.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    private String host;
    private int port;
}
