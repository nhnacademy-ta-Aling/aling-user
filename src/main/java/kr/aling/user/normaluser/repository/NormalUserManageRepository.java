package kr.aling.user.normaluser.repository;

import kr.aling.user.normaluser.entity.NormalUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 일반회원 CUD JpaRepository.
 *
 * @author : 이수정
 * @since : 1.0
 */
public interface NormalUserManageRepository extends JpaRepository<NormalUser, Long> {

}
