package kr.aling.user.normaluser.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 일반 회원가입 응답을 담는 Dto.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
public class CreateNormalUserResponseDto {

    private final String id;
    private final String name;
}
