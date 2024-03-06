package kr.aling.user.role.repository;

import kr.aling.user.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 권한 읽기 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface RoleReadRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
