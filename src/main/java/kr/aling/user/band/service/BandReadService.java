package kr.aling.user.band.service;

import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;

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
}
