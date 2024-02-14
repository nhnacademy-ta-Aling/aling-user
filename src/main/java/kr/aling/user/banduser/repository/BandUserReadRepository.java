package kr.aling.user.banduser.repository;

import kr.aling.user.banduser.entity.BandUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 그룹 회원을 조회 하기 위한 Repository.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface BandUserReadRepository extends JpaRepository<BandUser, Long>, BandUserReadRepositoryCustom {

    /**
     * 특정 회원의 특정 그룹 회원 권한 보유 개수 count 쿼리.
     *
     * @param userNo 회원 번호
     * @param roleName 그룹 회원 권한 이름
     * @return 특정 회원의 특정 그룹 회원 권한 보유 개수
     */
    long countByAlingUser_UserNoAndBandUserRole_RoleName(Long userNo, String roleName);
}
