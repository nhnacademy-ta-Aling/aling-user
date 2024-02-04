package kr.aling.user.user.dummy;

import kr.aling.user.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 회원 테스트를 위한 더미.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class UserDummy {

    public static User dummyEncoder(PasswordEncoder passwordEncoder) {
        return User.builder()
                .id("test@test.com")
                .password(passwordEncoder.encode("nhn123!@#"))
                .name("NHN-Test")
                .address("경기도 성남시 분당구 대왕판교로 645번길 16")
                .build();
    }

    public static User dummy() {
        return User.builder()
                .id("testId@test.com")
                .password("testPwd")
                .name("testName")
                .address("testAddress")
                .build();
    }
}
