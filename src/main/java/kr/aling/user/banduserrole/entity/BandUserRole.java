package kr.aling.user.banduserrole.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 밴드 유저 권한 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "band_user_role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BandUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer bandUserRoleNo;

    @Column(name = "band_user_role_name")
    private String roleName;

    @Builder
    public BandUserRole(Integer bandUserRoleNo, String roleName) {
        this.bandUserRoleNo = bandUserRoleNo;
        this.roleName = roleName;
    }
}
