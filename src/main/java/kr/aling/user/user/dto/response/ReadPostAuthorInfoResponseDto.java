package kr.aling.user.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 게시물 작성자에 대한 정보 응답 객체입니다.
 *
 * @author : 이성준
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
public class ReadPostAuthorInfoResponseDto {

    private Long postNo;
    private ReadUserInfoResponseDto userInfo;
}
