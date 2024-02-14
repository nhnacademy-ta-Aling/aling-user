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
@Entity
@Getter
@Table(name = "company_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyUser {

    @Id
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

    /**
     * 법인회원가입시 필요한 생성자.
     *
     * @param alingUser user entity
     * @param registrationNo 사업자등록번호
     * @param companySize 법인규모
     * @param sector 업종
     */
    @Builder
    public CompanyUser(AlingUser alingUser, String registrationNo, String companySize, String sector) {
        this.alingUser = alingUser;
        this.registrationNo = registrationNo;
        this.companySize = companySize;
        this.sector = sector;
        this.userNo = alingUser.getUserNo();
        this.employee = 0;
        this.salary = 0;
    }
}
