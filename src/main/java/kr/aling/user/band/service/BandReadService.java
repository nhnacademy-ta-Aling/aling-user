package kr.aling.user.band.service;

import java.util.List;
import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoWithBandUserResponseDto;
import kr.aling.user.band.dto.response.external.GetBandPostTypeResponseDto;
import kr.aling.user.common.dto.PageResponseDto;
import org.springframework.data.domain.Pageable;

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
    GetBandInfoWithBandUserResponseDto getBandDetailInfo(String bandName, Long userNo);

    /**
     * 그룹 명을 통해 그룹 정보 목록 조회 메서드.
     *
     * @param bandName 그룹명
     * @param pageable pageable
     * @return 그룹 정보 dto list
     */
    PageResponseDto<GetBandInfoResponseDto> getSearchBandInfoList(String bandName, Pageable pageable);

    /**
     * 특정 회원이 가입한 그룹 정보 목록 조회 메서드.
     *
     * @param userNo 회원 번호
     * @return 해당 회원이 가입한 그룹 정보 목록
     */
    List<GetBandDetailInfoResponseDto> getJoinedBandInfoList(Long userNo);

    /**
     * 특정 그룹의 그룹 게시글 분류 리스트를 조회하는 메서드 입니다.
     *
     * @param bandName 그룹 명
     * @return 그룹 게시글 분류 정보 dto 리스트
     */
    List<GetBandPostTypeResponseDto> getBandPostTypeList(String bandName);
}
