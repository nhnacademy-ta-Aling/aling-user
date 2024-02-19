package kr.aling.user.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원의 간단한 정보 응답 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetUserSimpleInfoResponseDto {
    private Long userNo;
    private Long fileNo;
    private String name;
}
