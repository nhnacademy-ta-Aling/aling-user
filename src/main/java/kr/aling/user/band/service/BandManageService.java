package kr.aling.user.band.service;

import kr.aling.user.band.dto.request.CreateBandRequestDto;

/**
 * 그룹(Band)을 관리 하는 Service.
 *
 * @author 정유진
 * @since 1.0
 **/
public interface BandManageService {

    /**
     * 그룹 생성을 위한 메서드.
     *
     * @param userNo 요청 회원 번호
     * @param createBandRequestDto 그룹 생성 관련 dto
     */
    void makeBand(Long userNo, CreateBandRequestDto createBandRequestDto);
}
