package kr.aling.user.band.service;

import kr.aling.user.band.dto.request.CreateBandPostTypeRequestDto;
import kr.aling.user.band.dto.request.CreateBandRequestDto;
import kr.aling.user.band.dto.request.ModifyBandRequestDto;

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

    /**
     * 그룹 정보 수정을 위한 메서드 입니다.
     *
     * @param bandName 수정할 그룹 이름
     * @param modifyBandRequestDto 수정할 정보를 담은 dto
     */
    void updateBandInfo(String bandName, ModifyBandRequestDto modifyBandRequestDto);

    /**
     * 그룹 삭제를 위한 메서드입니다. <br>
     * soft delete를 진행합니다.
     *
     * @param bandName 삭제할 그룹 이름
     */
    void removeBand(String bandName);

    /**
     * 그룹 게시글 분류를 생성하기 위한 메서드입니다.
     *
     * @param bandName 그룹 게시글 분류를 생성할 그룹 명
     * @param requestDto 그룹 게시글 분류 생성을 위한 정보를 담은 dto
     */
    void makeBandCategory(String bandName, CreateBandPostTypeRequestDto requestDto);
}
