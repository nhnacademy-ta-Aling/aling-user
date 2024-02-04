package kr.aling.user.banduser.repository;

import kr.aling.user.banduser.entity.BandUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface BandUserManageRepository extends JpaRepository<BandUser, Long> {
}
