package kr.aling.user.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저 정보에 대한 요청 객체입니다.
 *
 * @author : 이성준
 * @since : 1.0
 */
@Getter
@NoArgsConstructor
public class ReadAuthorInfoRequestDto {

    private Long userNo;
}
