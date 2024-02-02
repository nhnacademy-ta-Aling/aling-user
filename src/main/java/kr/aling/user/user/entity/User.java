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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Some description here.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userNo;

    @Column(name = "user_id")
    private String id;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_is_delete")
    private Boolean isDelete;

    @Column(name = "user_delete_at")
    private LocalDateTime deleteAt;

    @Column(name = "file_no")
    private Long fileNo;

    @Column(name = "user_is_block")
    private Boolean isBlock;

    @Column(name = "user_description")
    private String description;

    @Column(name = "user_address")
    private String address;

    @Column(name = "user_fix_post_no")
    private Long fixPostNo;

    @PrePersist
    public void prePersist() {
        this.isBlock = Objects.isNull(this.isBlock) ? Boolean.FALSE : this.isBlock;
        this.isDelete = Objects.isNull(this.isDelete) ? Boolean.FALSE : this.isDelete;
    }
}
