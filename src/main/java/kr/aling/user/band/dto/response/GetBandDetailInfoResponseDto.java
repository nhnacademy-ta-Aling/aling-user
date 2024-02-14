package kr.aling.user.band.dto.response;

import java.util.List;
import kr.aling.user.user.dto.response.GetBandUserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 그룹의 상세 정보를 반환하기 위한 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/

@Getter
@AllArgsConstructor
public class GetBandDetailInfoResponseDto {
    private String name;
    private Long fileNo;
    private String info;
    private Boolean isEnter;
    private Boolean isViewContent;
    private Boolean isUpload;
    private List<GetBandUserInfoResponseDto> bandUserList;
}
