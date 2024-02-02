package kr.aling.user.normaluser.entity;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import kr.aling.user.normaluser.type.NormalUserOpen;
import kr.aling.user.user.entity.User;
import kr.aling.user.wantjobtype.entity.WantJobType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일반회원 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "normal_user")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NormalUser {

    @Id
    private Long userNo;

    @MapsId("userNo")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @ManyToOne
    @JoinColumn(name = "want_job_type_no")
    private WantJobType wantJobType;

    @Column(name = "normal_user_phone_no")
    private String phoneNo;

    @Column(name = "normal_user_birth")
    private LocalDate birth;

    @Column(name = "normal_user_edu_is_open")
    private String eduIsOpen;

    @Column(name = "normal_user_career_is_open")
    private String careerIsOpen;

    @Column(name = "normal_user_cert_is_open")
    private String certIsOpen;

    @Column(name = "normal_user_resume_is_open")
    private String resumeIsOpen;

    @Column(name = "normal_user_cover_letter_is_open")
    private String coverLetterIsOpen;

    @Column(name = "normal_user_tech_skill_is_open")
    private String techSkillIsOpen;

    /**
     * PrePersist.
     */
    @PrePersist
    public void prePersist() {
        this.eduIsOpen = Objects.isNull(this.eduIsOpen)
                ? NormalUserOpen.ALL.getValue() : this.eduIsOpen;
        this.careerIsOpen = Objects.isNull(this.careerIsOpen)
                ? NormalUserOpen.ALL.getValue() : this.careerIsOpen;
        this.certIsOpen = Objects.isNull(this.certIsOpen)
                ? NormalUserOpen.ALL.getValue() : this.certIsOpen;
        this.resumeIsOpen = Objects.isNull(this.resumeIsOpen)
                ? NormalUserOpen.ALL.getValue() : this.resumeIsOpen;
        this.coverLetterIsOpen = Objects.isNull(this.coverLetterIsOpen)
                ? NormalUserOpen.ALL.getValue() : this.coverLetterIsOpen;
        this.techSkillIsOpen = Objects.isNull(this.techSkillIsOpen)
                ? NormalUserOpen.ALL.getValue() : this.techSkillIsOpen;

    }
}
