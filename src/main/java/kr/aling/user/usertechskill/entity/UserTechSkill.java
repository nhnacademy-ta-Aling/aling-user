package kr.aling.user.usertechskill.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import kr.aling.user.normaluser.entity.NormalUser;
import kr.aling.user.techskill.entity.TechSkill;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("normalUserNo")
    @JoinColumn(name = "normal_user_no")
    private NormalUser normalUser;

    @ManyToOne(fetch = FetchType.LAZY)
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
        private Long normalUserNo;
        @Column(name = "tech_skill_no")
        private Integer techSkillNo;
    }
}
