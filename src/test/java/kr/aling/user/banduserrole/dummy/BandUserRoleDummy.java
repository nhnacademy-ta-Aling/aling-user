package kr.aling.user.banduserrole.dummy;

import kr.aling.user.banduserrole.entity.BandUserRole;

/**
 * Some description here.
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
}