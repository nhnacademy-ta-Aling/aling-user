package kr.aling.user.postscrap.entity;

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
import kr.aling.user.common.base.BaseCreateTimeEntity;
import kr.aling.user.user.entity.AlingUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시물 스크랩 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "post_scrap")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrap extends BaseCreateTimeEntity {

    @EmbeddedId
    private Pk pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userNo")
    @JoinColumn(name = "aling_user_no")
    private AlingUser alingUser;

    /**
     * 게시물 스크랩의 pk 클래스입니다.
     */
    @Builder
    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {

        private Long userNo;

        @Column(name = "post_no")
        private Long postNo;
    }

}
