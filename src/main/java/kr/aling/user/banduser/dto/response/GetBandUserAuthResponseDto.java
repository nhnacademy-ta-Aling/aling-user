package kr.aling.user.banduser.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 그룹 회원 권한 조회를 위한 dto. <br>
 * gateway 그룹 회원 권한 관련 필터에서 사용됩니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetBandUserAuthResponseDto {
    private String bandUserRoleName;
}
