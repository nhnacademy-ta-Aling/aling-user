package kr.aling.user.band.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import kr.aling.user.common.base.BaseCreateTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Band extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * 그룹 생성을 위한 빌더.
     *
     * @param name          그룹 이름
     * @param info          그룹 소개글
     * @param isEnter       즉시 가입 여부
     * @param isViewContent 게시글 외부 노출 여부
     * @param fileNo        그룹 프로필 사진 파일 번호
     */
    @Builder
    public Band(String name, String info, Boolean isEnter, Boolean isViewContent, Long fileNo) {
        this.name = name;
        this.info = info;
        this.isEnter = isEnter;
        this.isViewContent = isViewContent;
        this.fileNo = fileNo;
    }

    /**
     * 그룹 수정을 위한 메서드.
     *
     * @param name          그룹명
     * @param info          그룹 소개글
     * @param isEnter       그룹 즉시 가입 여부
     * @param isViewContent 그룹 게시글 공개 여부
     * @param fileNo        그룹 프로필 사진 파일 번호
     */
    public void updateBand(String name, String info, Boolean isEnter, Boolean isViewContent, Long fileNo) {
        this.name = name;
        this.info = info;
        this.isEnter = isEnter;
        this.isViewContent = isViewContent;
        this.fileNo = fileNo;
    }

    /**
     * 그룹을 삭제 처리 하는 메서드. soft delete 이며, 이미 삭제된 그룹은 되살릴 수 없습니다.
     */
    public void deleteBand() {
        this.isDelete = true;
    }

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
