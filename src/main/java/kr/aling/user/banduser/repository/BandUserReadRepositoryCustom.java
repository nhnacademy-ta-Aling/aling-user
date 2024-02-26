package kr.aling.user.banduser.repository;

import java.util.Optional;
import kr.aling.user.banduser.dto.response.GetBandUserAndUserInfoResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserAuthResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserInfoResponseDto;
import kr.aling.user.banduser.entity.BandUser;
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
     * @param bandName 그룹명
     * @param pageable pageable
     * @return 그룹 회원 정보 dto 페이지
     */
    Page<GetBandUserAndUserInfoResponseDto> getBandUserListByBandName(String bandName, Pageable pageable);

    /**
     * 회원 번호와 그룹 명을 통해 그룹 회원 정보를 조회 하는 메서드.
     *
     * @param bandName 그룹 명
     * @param userNo 회원 번호
     * @return 그룹 회원 정보 dto
     */
    Optional<GetBandUserInfoResponseDto> getBandUserInfoByUserNoAndBandName(String bandName, Long userNo);

    /**
     * 그룹 번호와 회원 번호로 그룹 회원 권한을 조회하는 메서드입니다.
     * gateway 그룹 회원 권한 필터 로직을 위한 메서드입니다.
     *
     * @param bandNo 그룹 번호
     * @param userNo 회원 번호
     * @return 그룹 회원 권한 정보 dto
     */
    Optional<GetBandUserAuthResponseDto> getBandUserInfoByBandNoAndUserNo(Long bandNo, Long userNo);

    /**
     * 그룹 명과 회원 번호로 그룹 회원을 조회 하기 위한 메서드입니다.<br>
     * 그룹 회원 수정 시 사용 됩니다.
     *
     * @param bandName 그룹 명
     * @param userNo 회원 번호
     * @return 그룹 회원 entity
     */
    Optional<BandUser> findBandUserByBandNameAndUserNo(String bandName, Long userNo);

    /**
     * 특정 회원의 특정 그룹 회원 권한 보유 개수 count 쿼리.
     *
     * @param userNo 회원 번호
     * @param roleName 그룹 회원 권한 이름
     * @return 특정 회원의 특정 그룹 회원 권한 보유 개수
     */
    long countByUserNoAndBandUserRoleName(Long userNo, String roleName);
}
