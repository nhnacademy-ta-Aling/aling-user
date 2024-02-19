package kr.aling.user.band.dto.response;

import kr.aling.user.banduser.dto.response.GetBandUserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 그룹 상세 정보 응답 dto. <br>
 * GetBandDetailInfoResponseDto 와 같은 역할.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetBandInfoWithBandUserResponseDto {
    private GetBandInfoResponseDto bandInfo;
    private GetBandUserInfoResponseDto bandUserInfo;
}
