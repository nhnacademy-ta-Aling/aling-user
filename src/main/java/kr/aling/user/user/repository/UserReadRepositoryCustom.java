package kr.aling.user.user.repository;

import java.util.List;
import java.util.Optional;
import kr.aling.user.user.dto.response.LoginInfoResponseDto;
import kr.aling.user.user.entity.AlingUser;
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
     * 이메일로 회원 조회.
     *
     * @param email 이메일
     * @return 로그인하는 회원 정보
     */
    Optional<LoginInfoResponseDto> findByEmailForLogin(String email);

    /**
     * 회원 번호로 권한 조회.
     *
     * @param userNo 회원 번호
     * @return 권한 리스트
     */
    List<String> findRolesByUserNo(Long userNo);

    /**
     * 회원 번호로 회원을 조회 하는 메서드.
     *
     * @param userNo 회원 번호
     * @return optional 회원 엔티티
     */
    Optional<AlingUser> getByUserNo(Long userNo);
}
