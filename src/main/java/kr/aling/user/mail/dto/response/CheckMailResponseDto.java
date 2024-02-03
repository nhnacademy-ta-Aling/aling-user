package kr.aling.user.mail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 이메일 인증 응답을 담는 Dto.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
public class CheckMailResponseDto {

    private int authNumber;
}
