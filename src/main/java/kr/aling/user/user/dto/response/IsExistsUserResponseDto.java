package kr.aling.user.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 존재 여부 응답을 담는 Dto.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
public class IsExistsUserResponseDto {

    private final Boolean isExists;
}
