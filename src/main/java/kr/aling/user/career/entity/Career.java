package kr.aling.user.career.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * 경력 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "career")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long careerNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "normal_user_no")
    private NormalUser normalUser;

    @Column(name = "career_name")
    private String name;

    @Column(name = "career_start_at")
    private LocalDate startAt;

    @Column(name = "career_end_at")
    private LocalDate endAt;

    @Column(name = "career_position")
    private String position;

    @Column(name = "career_content")
    private String content;

    @Column(name = "career_location")
    private String location;
}
