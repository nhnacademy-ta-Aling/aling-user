package kr.aling.user.companyuser.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import kr.aling.user.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 기업회원 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "company_user")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyUser {

    @Id
    private Long userNo;

    @MapsId("userNo")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @Column(name = "company_user_company_size")
    private String companySize;

    @Column(name = "company_user_employee")
    private Integer employee;

    @Column(name = "company_user_salary")
    private Integer salary;

    @Column(name = "company_user_sector")
    private String sector;

}
