package kr.aling.user.banduser.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 그룹 회원 정보 응답 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetBandUserInfoResponseDto {

    private Long bandUserNo;
    private Integer bandUserRoleNo;
}
