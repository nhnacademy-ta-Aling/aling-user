package kr.aling.user.follow.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import kr.aling.user.user.entity.AlingUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 팔로우 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "follow")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long followNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aling_user_no")
    private AlingUser alingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_aling_user_no")
    private AlingUser followingAlingUser;

    @Column(name = "follow_is_alarm")
    private Boolean isAlarm;

    /**
     * PrePersist.
     */
    @PrePersist
    public void prePersist() {
        this.isAlarm = Objects.isNull(this.isAlarm) ? Boolean.FALSE : this.isAlarm;
    }
}
