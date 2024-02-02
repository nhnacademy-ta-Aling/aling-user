package kr.aling.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * UserApplication.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ConfigurationPropertiesScan
@SpringBootApplication
@EnableJpaAuditing
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
