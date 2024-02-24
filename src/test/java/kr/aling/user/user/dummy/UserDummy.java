package kr.aling.user.user.dummy;

import kr.aling.user.user.entity.AlingUser;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 회원 테스트를 위한 더미.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class UserDummy {

    public static AlingUser dummyEncoder(PasswordEncoder passwordEncoder) {
        return AlingUser.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("nhn123!@#"))
                .name("NHN-Test")
                .address("경기도 성남시 분당구 대왕판교로 645번길 16")
                .build();
    }

    public static AlingUser dummy() {
        return AlingUser.builder()
                .email("testId@test.com")
                .password("testPwd")
                .name("testName")
                .address("testAddress")
                .build();
    }
}
