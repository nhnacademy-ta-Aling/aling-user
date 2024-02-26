package kr.aling.user.banduserrole.dummy;

import kr.aling.user.banduserrole.entity.BandUserRole;

/**
 * 그룹 회원 권한 더미.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandUserRoleDummy {
    public static BandUserRole dummyCreator() {
        return BandUserRole.builder()
                .bandUserRoleNo(1)
                .roleName("BAND_ROLE_CREATOR")
                .build();
    }

    public static BandUserRole dummyUser() {
        return BandUserRole.builder()
                .bandUserRoleNo(3)
                .roleName("BAND_ROLE_USER")
                .build();
    }

    public static BandUserRole dummyDefaultBandUserRole() {
        return BandUserRole.builder()
                .roleName("role name")
                .build();
    }
}