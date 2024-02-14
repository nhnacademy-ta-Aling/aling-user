package kr.aling.user.band.service;

import java.util.List;
import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.user.dto.response.GetBandInfoResponseDto;

/**
 * 그룹(Band)을 조회 하기 위한 Service.
 *
 * @author 정유진
 * @since 1.0
 **/
public interface BandReadService {

    /**
     * 그룹명 중복 검사를 위한 메서드.
     *
     * @param bandName 그룹명
     * @return 그룹명 중복 여부 정보 dto
     */
    ExistsBandNameResponseDto existBandName(String bandName);

    /**
     * 그룹 명을 통해 그룹 상세 정보 조회 메서드.
     *
     * @param bandName 그룹명
     * @return 그룹 상세 정보 dto
     */
    GetBandDetailInfoResponseDto getBandDetailInfo(String bandName);

    /**
     * 그룹 명을 통해 그룹 정보 목록 조회 메서드.
     *
     * @param bandName 그룹명
     * @return 그룹 정보 dto list
     */
    List<GetBandInfoResponseDto> getSearchBandInfoList(String bandName);
}
