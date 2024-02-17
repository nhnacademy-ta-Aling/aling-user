package kr.aling.user.band.repository;

import java.util.List;
import java.util.Optional;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
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
    Optional<GetBandDetailInfoResponseDto> getBandDetailInfoByBandName(String bandName, Long userNo);

    /**
     * 그룹 명을 통해 그룹 목록을 조회 하는 메서드.
     *
     * @param bandName 그룹 명
     * @return 그룹 정보 리스트
     */
    List<GetBandInfoResponseDto> getSearchBandInfoListByBandName(String bandName);

    /**
     * 해당 회원이 가입한 그룹 목록 조회.
     *
     * @param userNo 회원 번호
     * @return 해당 회원이 가입한 그룹 목록
     */
    List<GetBandInfoResponseDto> getJoinedBandInfoListByUserNo(Long userNo);
}
