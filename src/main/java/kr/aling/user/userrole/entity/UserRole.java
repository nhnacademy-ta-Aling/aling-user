package kr.aling.user.userrole.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import kr.aling.user.role.entity.Role;
import kr.aling.user.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저 권한 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "user_role")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole {

    @EmbeddedId
    private Pk pk;

    @ManyToOne
    @MapsId("userNo")
    @JoinColumn(name = "user_no")
    private User user;

    @ManyToOne
    @MapsId("roleNo")
    @JoinColumn(name = "role_no")
    private Role role;

    /**
     * 유저 권한의 pk 클래스입니다.
     */
    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "user_no")
        private Long userNo;

        @Column(name = "role_no")
        private Integer roleNo;
    }
}
