package kr.aling.user.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetBandUserInfoResponseDto {
    private Long userNo;
    private Long fileNo;
    private String name;
}
