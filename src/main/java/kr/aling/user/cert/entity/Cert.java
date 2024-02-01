package kr.aling.user.cert.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kr.aling.user.normaluser.entity.NormalUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 자격증 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "cert")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cert {
    @Id
    @Column
    private Long certNo;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private NormalUser normalUser;

    @Column(name = "cert_name")
    private String name;

    @Column(name = "cert_organ")
    private String organ;

    @Column(name = "cert_issue_at")
    private LocalDate issueAt;

    @Column(name = "cert_score")
    private String score;

}
