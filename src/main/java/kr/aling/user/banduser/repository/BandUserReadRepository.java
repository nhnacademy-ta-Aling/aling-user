package kr.aling.user.banduser.repository;

import java.util.Optional;
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
     * 그룹 삭제 시, 그룹 creator를 탈퇴 처리하기 위한 메서드입니다.
     *
     * @param roleName 그룹 회원 권한 이름
     * @return optional 그룹 회원 엔티티
     */
    Optional<BandUser> findByBand_NameAndBandUserRole_RoleName(String bandName, String roleName);

}
