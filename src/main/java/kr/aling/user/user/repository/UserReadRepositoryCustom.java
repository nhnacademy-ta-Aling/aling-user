package kr.aling.user.user.repository;

import java.util.List;
import kr.aling.user.user.dto.response.GetBandInfoResponseDto;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * QueryDsl 사용을 위한 커스텀 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface UserReadRepositoryCustom {

    /**
     * 이메일 중복 검사.
     *
     * @param email 이메일
     * @return 중복 여부(중복시 true)
     */
    Boolean existsByEmail(String email);

    /**
     * 해당 회원이 가입한 그룹 목록 조회.
     *
     * @param userNo 회원 번호
     * @return 해당 회원이 가입한 그룹 목록
     */
    List<GetBandInfoResponseDto> getJoinedBandInfoListByUserNo(Long userNo);

}
