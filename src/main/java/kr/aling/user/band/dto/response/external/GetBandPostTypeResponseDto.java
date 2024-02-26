package kr.aling.user.band.dto.response.external;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * post 서버 에서 그룹 게시글 분류 정보를 받아 오기 위한 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class GetBandPostTypeResponseDto {
    private String name;
}
