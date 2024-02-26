package kr.aling.user.banduser.service;

import kr.aling.user.banduser.dto.response.GetBandUserAndUserInfoResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserAuthResponseDto;
import kr.aling.user.banduser.dto.response.GetPostWriterResponseDto;
import kr.aling.user.common.dto.PageResponseDto;
import org.springframework.data.domain.Pageable;

/**
 * 그룹 회원 조회 서비스.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface BandUserReadService {

    /**
     * 그룹 번호를 통해 해당 그룹의 그룹 회원 리스트를 조회 하는 메서드.
     *
     * @param bandName   그룹 명
     * @param pageable pageable
     * @return 그룹 회원 정보 dto 페이
     */
    PageResponseDto<GetBandUserAndUserInfoResponseDto> getBandUserList(String bandName, Pageable pageable);

    /**
     * 그룹 회원 권한 정보를 조회 하기 위한 메서드입니다.
     * gateway 그룹 회원 권한 필터를 위한 메서드입니다.
     *
     * @param bandNo 그룹 번호
     * @param userNo 회원 번호
     * @return 그룹 회원 권한 정보를 담은 dto
     */
    GetBandUserAuthResponseDto getBandUserInfo(Long bandNo, Long userNo);
    /**
     * 그룹 회원 번호를 가지고 회원 정보를 조회 하는 메서드.
     *
     * @param bandUserNo 그룹 회원 번호
     * @return 회원 정보 Dto.
     */
    GetPostWriterResponseDto getPostWriterInfo(Long bandUserNo);
}
