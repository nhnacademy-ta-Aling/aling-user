package kr.aling.user.user.repository;

import kr.aling.user.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 유저 관리 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface UserManageRepository extends JpaRepository<User, Long> {

}
