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
import kr.aling.user.user.entity.AlingUser;
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
    private AlingUser alingUser;

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
     * @param alingUser    회원
     */
    @Builder
    public BandUser(BandUserRole bandUserRole, Band band, AlingUser alingUser) {
        this.bandUserRole = bandUserRole;
        this.band = band;
        this.alingUser = alingUser;
    }

    public void updateBandUserRole(BandUserRole bandUserRole) {
        this.bandUserRole = bandUserRole;
    }

    public void updateBandUserBlock() {
        this.isBlock = !this.isBlock;
    }

    public void updateBandUserDelete() {
        this.isDelete = !this.isDelete;
    }

    /**
     * 그룹 회원 삭제를 위한 메서드. 그룹 회원은 삭제 후 복구 되지 않습니다.
     */
    public void deleteBandUser() {
        this.isDelete = true;
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
        this.enterAt = Objects.isNull(this.enterAt)
                ? LocalDateTime.now() : this.enterAt;
    }
}
