package kr.aling.user.band.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 그룹 이름 중복 검사 응답 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@AllArgsConstructor
@Getter
public class ExistsBandNameResponseDto {
    private Boolean isExist;
}
