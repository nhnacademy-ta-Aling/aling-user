package kr.aling.user.role.service;

import kr.aling.user.role.entity.Role;

/**
 * 권한 조회 Service interface.
 *
 * @author 이수정
 * @since 1.0
 */
public interface RoleReadService {

    /**
     * 권한명으로 권한을 조회해 반환합니다.
     *
     * @param name 권한명
     * @return 조회된 권한 엔티티
     * @author 이수정
     * @since 1.0
     */
    Role getRoleByName(String name);
}
