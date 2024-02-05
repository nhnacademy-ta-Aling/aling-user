package kr.aling.user.common.enums;

import lombok.Getter;

/**
 * 그룹 회원 권한 Enum.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
public enum BandUserRoleEnum {
    CREATOR("BAND_ROLE_CREATOR"), ADMIN("BAND_ROLE_ADMIN"), USER("BAND_ROLE_USER");

    private String roleName;

    BandUserRoleEnum(String roleName) {
        this.roleName = roleName;
    }
}
