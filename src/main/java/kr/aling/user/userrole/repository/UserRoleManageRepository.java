package kr.aling.user.userrole.repository;

import kr.aling.user.userrole.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 유저권한 관리 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface UserRoleManageRepository extends JpaRepository<UserRole, UserRole.Pk> {
}
