package kr.aling.user.usertechskill.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import kr.aling.user.techskill.entity.TechSkill;
import kr.aling.user.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일반회원 테크 스킬 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "user_tech_skill")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTechSkill {

    @EmbeddedId
    private Pk pk;

    @ManyToOne
    @MapsId("userNo")
    @JoinColumn(name = "normal_user_no")
    private User user;

    @ManyToOne
    @MapsId("techSkillNo")
    @JoinColumn(name = "tech_skill_no")
    private TechSkill techSkill;

    /**
     * 유저 권한의 pk 클래스입니다.
     */
    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {

        @Column(name = "normal_user_no")
        private Long userNo;
        @Column(name = "tech_skill_no")
        private Integer techSkillNo;
    }
}
