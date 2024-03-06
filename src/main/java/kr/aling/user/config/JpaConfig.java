package kr.aling.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA 관련 Config.
 *
 * @author 정유진
 * @since 1.0
 **/
@EnableJpaAuditing
@Configuration
public class JpaConfig {
}
