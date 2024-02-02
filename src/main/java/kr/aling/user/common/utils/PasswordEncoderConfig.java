package kr.aling.user.common.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The class for BCryptPasswordEncoder.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Configuration
public class PasswordEncoderConfig {

    /**
     * 회원가입과 로그인시 사용될 PasswordEncoder.
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
