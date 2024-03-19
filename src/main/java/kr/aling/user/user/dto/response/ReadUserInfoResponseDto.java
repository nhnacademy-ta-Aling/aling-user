package kr.aling.user.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 유저 정보에 대한 응답 객체입니다. 유저 식별 번호, 이름, 프로필 사진 경로가 포함되어있습니다.
 *
 * @author : 이성준
 * @since : 1.0
 */
@Getter
@Builder
@AllArgsConstructor
public class ReadUserInfoResponseDto {

    private Long userNo;
    private String username;
    private String profilePath;
}
