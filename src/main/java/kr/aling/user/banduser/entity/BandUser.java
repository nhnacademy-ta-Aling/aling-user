package kr.aling.user.banduser.entity;

import java.time.LocalDateTime;
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
import kr.aling.user.band.entity.Band;
import kr.aling.user.banduserrole.entity.BandUserRole;
import kr.aling.user.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 밴드에 가입된 회원 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "band_user")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BandUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long bandUserNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_user_role_no")
    private BandUserRole bandUserRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_no")
    private Band band;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aling_user_no")
    private User user;

    @Column(name = "band_user_enter_at")
    private LocalDateTime enterAt;

    @Column(name = "band_user_is_block")
    private Boolean isBlock;

    @Column(name = "band_user_block_reason")
    private String blockReason;

    @Column(name = "band_user_is_delete")
    private Boolean isDelete;

    /**
     * BandUser 생성을 위한 빌더.
     *
     * @param bandUserRole 그룹회원권한
     * @param band         그룹
     * @param user         회원
     * @param enterAt      가입일
     */
    @Builder
    public BandUser(BandUserRole bandUserRole, Band band, User user, LocalDateTime enterAt) {
        this.bandUserRole = bandUserRole;
        this.band = band;
        this.user = user;
        this.enterAt = enterAt;
    }

    /**
     * PrePersist.
     */
    @PrePersist
    public void prePersist() {
        this.isBlock = Objects.isNull(this.isBlock)
                ? Boolean.FALSE : this.isBlock;
        this.isDelete = Objects.isNull(this.isDelete)
                ? Boolean.FALSE : this.isDelete;
    }
}
