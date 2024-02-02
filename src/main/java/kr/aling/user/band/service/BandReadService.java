package kr.aling.user.band.service;

import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;

/**
 * 그룹(Band)을 조회 하기 위한 Service.
 *
 * @author 정유진
 * @since 1.0
 **/
public interface BandReadService {
    ExistsBandNameResponseDto existBandName(String bandName);
}
