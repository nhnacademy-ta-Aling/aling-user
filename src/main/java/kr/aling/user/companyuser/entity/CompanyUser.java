package kr.aling.user.companyuser.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import kr.aling.user.user.entity.AlingUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * 기업회원 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Builder
@AllArgsConstructor
@Entity
@Getter
@Table(name = "company_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyUser {

    @Id
    @Column(name = "aling_user_no")
    private Long userNo;

    @MapsId("userNo")
    @OneToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.MERGE)
    @JoinColumn(name = "aling_user_no")
    private AlingUser alingUser;

    @Column(name = "company_user_registration_no")
    private String registrationNo;

    @Column(name = "company_user_company_size")
    private String companySize;

    @Column(name = "company_user_employee")
    private Integer employee;

    @Column(name = "company_user_salary")
    private Integer salary;

    @Column(name = "company_user_sector")
    private String sector;
}
