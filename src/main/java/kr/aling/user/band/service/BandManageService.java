package kr.aling.user.band.service;

import kr.aling.user.band.dto.request.CreateBandPostTypeRequestDto;
import kr.aling.user.band.dto.request.CreateBandRequestDto;
import kr.aling.user.band.dto.request.ModifyBandPostTypeRequestDto;
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
     * @param userNo               요청 회원 번호
     * @param createBandRequestDto 그룹 생성 관련 dto
     */
    void makeBand(Long userNo, CreateBandRequestDto createBandRequestDto);

    /**
     * 그룹 정보 수정을 위한 메서드 입니다.
     *
     * @param bandName             수정할 그룹 이름
     * @param modifyBandRequestDto 수정할 정보를 담은 dto
     */
    void updateBandInfo(String bandName, ModifyBandRequestDto modifyBandRequestDto);

    /**
     * 그룹 삭제를 위한 메서드입니다. <br> soft delete를 진행합니다.
     *
     * @param bandName 삭제할 그룹 이름
     */
    void removeBand(String bandName);

    /**
     * 그룹 게시글 분류를 생성하기 위한 메서드입니다.
     *
     * @param bandName   그룹 게시글 분류를 생성할 그룹 명
     * @param requestDto 그룹 게시글 분류 생성을 위한 정보를 담은 dto
     */
    void makeBandPostType(String bandName, CreateBandPostTypeRequestDto requestDto);

    /**
     * 그룹 게시글 분류를 수정 하기 위한 메서드 입니다.
     *
     * @param bandName 그룹 명
     * @param postTypeNo 수정할 그룹 게시글 분류 번호
     * @param requestDto 그룹 게시글 분류 수정 정보를 담은 dto
     */
    void modifyBandPostType(String bandName, Long postTypeNo, ModifyBandPostTypeRequestDto requestDto);

    /**
     * 그룹 게시글 분류를 삭제 하기 위한 메서드입니다.<br>
     * 삭제되지 않은 그룹 게시글이 존재할 경우 그룹 게시글 분류가 삭제되지 않습니다.
     *
     * @param bandName 그룹 명
     * @param postTypeNo 삭제할 그룹 게시글 분류 번호
     */
    void deleteBandPostType(String bandName, Long postTypeNo);
}
