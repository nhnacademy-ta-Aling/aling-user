package kr.aling.user.banduser.repository;

import java.util.Optional;
import kr.aling.user.banduser.dto.response.GetBandUserInfoResponseDto;
import kr.aling.user.user.dto.response.GetUserSimpleInfoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 그룹 회원을 조회 하기 위한 Custom Repository.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoRepositoryBean
public interface BandUserReadRepositoryCustom {

    /**
     * 그룹 번호를 통해 해당 그룹 회원 리스트를 조회하는 메서드.
     *
     * @param bandNo 그룹 번호
     * @param pageable pageable
     * @return 그룹 회원 정보 dto 페이지
     */
    Page<GetUserSimpleInfoResponseDto> getBandUserListByBandNo(Long bandNo, Pageable pageable);

    /**
     * 회원 번호와 그룹 명을 통해 그룹 회원 정보를 조회 하는 메서드.
     *
     * @param bandName 그룹 명
     * @param userNo 회원 번호
     * @return 그룹 회원 정보 dto
     */
    Optional<GetBandUserInfoResponseDto> getBandUserInfoByUserNoAndBandName(String bandName, Long userNo);
}
