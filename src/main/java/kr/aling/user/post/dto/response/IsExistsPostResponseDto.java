package kr.aling.user.post.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시물 존재 여부를 반환하는 Dto.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
public class IsExistsPostResponseDto {

    private Boolean isExists;
}
