package kr.aling.user.companysales.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kr.aling.user.companyuser.entity.CompanyUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 기업 매출 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "company_sales")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanySales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long companySalesNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_user_no")
    private CompanyUser companyUser;

    @Column(name = "company_sales_quarter")
    private Integer quarter;

    @Column(name = "company_sales_year")
    private Integer year;

    @Column(name = "company_sales_sum")
    private Long sum;

}
