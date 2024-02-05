package kr.aling.user.banduser.repository;

import kr.aling.user.banduser.entity.BandUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 그룹 회원을 관리 하는 Repository.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface BandUserManageRepository extends JpaRepository<BandUser, Long> {
}
