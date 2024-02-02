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
import kr.aling.user.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
public class PostScrap {
    @EmbeddedId
    private Pk pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userNo")
    @JoinColumn(name = "user_no")
    private User user;

    /**
     * 게시물 스크랩의 pk 클래스입니다.
     */
    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "user_no")
        private Long userNo;

        @Column(name = "post_no")
        private Long postNo;
    }

}