package kr.aling.user.band.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 그룹 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "band")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Band {
    @Id
    @Column
    private Long bandNo;

    @Column(name = "band_name")
    private String name;

    @Column(name = "band_info")
    private String info;

    @Column(name = "band_is_enter")
    private Boolean isEnter;

    @Column(name = "band_is_view_content")
    private Boolean isViewContent;

    @Column(name = "band_is_upload")
    private Boolean isUpload;

    @Column(name = "band_is_delete")
    private Boolean isDelete;

    @Column(name = "file_no")
    private Long fileNo;

    @Column(name = "band_fix_post_no")
    private Long fixPostNo;

    /**
     * PrePersist.
     */
    @PrePersist
    public void prePersist() {
        this.isEnter = Objects.isNull(this.isEnter)
                ? Boolean.TRUE : this.isEnter;
        this.isViewContent = Objects.isNull(this.isViewContent)
                ? Boolean.TRUE : this.isViewContent;
        this.isUpload = Objects.isNull(this.isUpload)
                ? Boolean.FALSE : this.isUpload;
        this.isDelete = Objects.isNull(this.isDelete)
                ? Boolean.FALSE : this.isDelete;

    }

}
