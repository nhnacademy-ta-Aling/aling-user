package kr.aling.user.role.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 권한 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "role")
@Entity
public class Role {

    @Id
    @Column(name = "role_no")
    private Integer roleNo;

    @Column(name = "role_name")
    private String name;
}
