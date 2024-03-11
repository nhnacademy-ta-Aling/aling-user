package kr.aling.user.user.dto.resquest;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시물 작성자에 대한 정보 요청 객체입니다.
 *
 * @author : 이성준
 * @since : 1.0
 */
@Getter
@NoArgsConstructor
public class ReadPostAuthorInfoRequestDto {
    private Long postNo;

    private boolean isBandPost;
    private ReadAuthorInfoRequestDto authorInfo;
}
