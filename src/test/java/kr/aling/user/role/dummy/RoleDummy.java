package kr.aling.user.role.dummy;

import kr.aling.user.role.entity.Role;

/**
 * 테스트를 위한 Role 더미.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class RoleDummy {
    public static Role dummy() {
        return new Role(1, "ROLE_ADMIN");
    }
}
