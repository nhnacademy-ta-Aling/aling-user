package kr.aling.user.band.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 그룹 정보 응답 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetBandInfoResponseDto {

    private Long bandNo;
    private String name;
    private Long fileNo;
    private String info;
    private Boolean isEnter;
    private Boolean isViewContent;
    private Boolean isUpload;
}

