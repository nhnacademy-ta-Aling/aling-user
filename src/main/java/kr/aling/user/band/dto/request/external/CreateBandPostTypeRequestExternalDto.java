package kr.aling.user.band.dto.request.external;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * post 서버로 기본 게시글 분류 생성 요청을 보내기 위한 요청 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class CreateBandPostTypeRequestExternalDto {
    private Long bandNo;
    private String bandPostTypeName;
}
