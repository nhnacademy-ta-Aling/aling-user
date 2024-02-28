package kr.aling.user.banduser.dto.response;

import kr.aling.user.user.dto.response.GetUserSimpleInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 그룹 정보와 그룹에 관련된 본인의 정보를 조회하기 위한 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetBandUserAndUserInfoResponseDto {

    private GetBandUserInfoResponseDto bandUserInfo;
    private GetUserSimpleInfoResponseDto userInfo;
}
