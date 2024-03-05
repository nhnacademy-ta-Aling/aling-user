package kr.aling.user.postscrap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 게시물 스크랩 여부를 반환하는 Dto.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class IsExistsPostScrapResponseDto {

    private Boolean isScrap;
}
