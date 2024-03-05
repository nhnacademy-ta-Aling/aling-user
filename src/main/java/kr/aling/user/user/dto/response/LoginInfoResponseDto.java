package kr.aling.user.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 시 비밀번호를 매칭하기 위한 dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class LoginInfoResponseDto {

    private final Long userNo;
    private final String password;
}
