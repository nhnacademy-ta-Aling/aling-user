package kr.aling.user.banduser.service;

import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.user.dto.response.GetUserSimpleInfoResponseDto;
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
     * @param bandNo   그룹 번호
     * @param pageable pageable
     * @return 그룹 회원 정보 dto 페이
     */
    PageResponseDto<GetUserSimpleInfoResponseDto> getBandUserList(Long bandNo, Pageable pageable);
}
