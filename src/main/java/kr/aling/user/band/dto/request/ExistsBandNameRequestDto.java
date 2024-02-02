package kr.aling.user.band.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 그룹 이름 중복 검사 응답 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoArgsConstructor
@Getter
public class ExistsBandNameRequestDto {
    private String bandName;
}
