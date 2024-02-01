package kr.aling.user.edu.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kr.aling.user.edutype.entity.EduType;
import kr.aling.user.normaluser.entity.NormalUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 학력 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "edu")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Edu {
    @Id
    @Column
    private Long eduNo;

    @ManyToOne
    @JoinColumn(name = "edu_type_no")
    private EduType eduType;

    @ManyToOne
    @JoinColumn(name = "normal_user_no")
    private NormalUser normalUser;

    @Column(name = "edu_name")
    private String name;

    @Column(name = "edu_enter_at")
    private LocalDate enterAt;

    @Column(name = "edu_graduate_at")
    private LocalDate graduateAt;
}
