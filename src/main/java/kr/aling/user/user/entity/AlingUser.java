package kr.aling.user.user.entity;

import java.time.LocalDateTime;
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
 * 유저 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "aling_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlingUser extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aling_user_no")
    private Long userNo;

    @Column(name = "aling_user_id")
    private String id;

    @Column(name = "aling_user_password")
    private String password;

    @Column(name = "aling_user_name")
    private String name;

    @Column(name = "aling_user_is_delete")
    private Boolean isDelete;

    @Column(name = "aling_user_delete_at")
    private LocalDateTime deleteAt;

    @Column(name = "file_no")
    private Long fileNo;

    @Column(name = "aling_user_is_block")
    private Boolean isBlock;

    @Column(name = "aling_user_description")
    private String description;

    @Column(name = "aling_user_address")
    private String address;

    @Column(name = "aling_user_fix_post_no")
    private Long fixPostNo;

    /**
     * 회원 생성을 위한 빌더.
     *
     * @param id       아이디
     * @param password 비밀번호
     * @param name     이름
     * @param address  주소
     */
    @Builder
    public AlingUser(String id, String password, String name, String address) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.address = address;
    }

    /**
     * PrePersist.
     */
    @PrePersist
    public void prePersist() {
        this.isBlock = Objects.isNull(this.isBlock) ? Boolean.FALSE : this.isBlock;
        this.isDelete = Objects.isNull(this.isDelete) ? Boolean.FALSE : this.isDelete;
    }
}
