package kr.aling.user.banduserrole.repository;

import java.util.Optional;
import kr.aling.user.banduserrole.entity.BandUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 그룹 회원 권한을 조회 하기 위한 Repository.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface BandUserRoleReadRepository
        extends JpaRepository<BandUserRole, Integer>, BandUserRoleReadRepositoryCustom {

    /**
     * 그룹 회원 권한 이름으로 그룹 회원 권한을 가져오는 쿼리문.
     *
     * @param roleName 그룹 회원 권한 이름
     * @return 그룹 회원 권한
     */
    Optional<BandUserRole> findByRoleName(String roleName);
}
