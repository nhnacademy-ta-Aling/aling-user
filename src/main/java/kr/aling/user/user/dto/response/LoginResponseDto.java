package kr.aling.user.user.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 시 되돌려줄 정보가 담긴 dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private final Long userNo;
    private final List<String> roles;
}
