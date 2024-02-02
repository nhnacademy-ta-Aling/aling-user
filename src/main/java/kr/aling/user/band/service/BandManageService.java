package kr.aling.user.band.service;

import kr.aling.user.band.dto.request.CreateBandRequestDto;

/**
 * 그룹(Band)을 관리 하는 Service.
 *
 * @author 정유진
 * @since 1.0
 **/
public interface BandManageService {
    void makeBand(Long userNo, CreateBandRequestDto createBandRequestDto);
}
