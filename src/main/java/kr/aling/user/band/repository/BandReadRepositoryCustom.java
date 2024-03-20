package kr.aling.user.band.repository;

import java.util.List;
import java.util.Optional;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
import kr.aling.user.band.entity.Band;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 그룹(Band)을 조회 하는 Repository Custom.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoRepositoryBean
public interface BandReadRepositoryCustom {

    /**
     * 그룹 명을 통해 그룹 상세 정보를 조회 하는 메서드.
     *
     * @param bandName 그룹 명
     * @return Optional 그룹 상세 정보
     */
    Optional<GetBandInfoResponseDto> getBandDetailInfoByBandName(String bandName);

    /**
     * 그룹 명을 통해 그룹 목록을 조회 하는 메서드.
     *
     * @param bandName 그룹 명
     * @param pageable pageable
     * @return 그룹 정보 dto 페이지
     */
    Page<GetBandInfoResponseDto> getSearchBandInfoListByBandName(String bandName, Pageable pageable);

    /**
     * 해당 회원이 가입한 그룹 목록 조회.
     *
     * @param userNo 회원 번호
     * @return 해당 회원이 가입한 그룹 목록
     */
    List<GetBandDetailInfoResponseDto> getJoinedBandInfoListByUserNo(Long userNo);

    /**
     * 그룹 명으로 그룹 조회. <br> 삭제된 그룹일 경우 조회 되지 않습니다.
     *
     * @param bandName 그룹 명
     * @return Optional 그룹 엔티티
     */
    Optional<Band> getByName(String bandName);

    /**
     * 그룹 명으로 그룹의 존재 여부 확인. <br> 없는 그룹 명 이거나 삭제된 그룹일 경우 false 를 반환 합니다.
     *
     * @param bandName 그룹 명
     * @return 그룹 존재 여부
     */
    boolean existsNonDeleteBandByName(String bandName);

    /**
     * 그룹에 가입 되어 있는 그룹 회원 수 조회.<br> 그룹 에서 추방 되었거나 탈퇴된 그룹 회원은 카운트 되지 않습니다.
     *
     * @param bandName 그룹 명
     * @return 그룹 내 그룹 회원 수
     */
    long getCountBandUser(String bandName);
}
