package kr.aling.user.resume.entity;

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
 * 이력서 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "resume")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume {
    @Id
    @Column
    private Long resumeNo;

    @ManyToOne
    @JoinColumn(name = "normal_user_no")
    private NormalUser normalUser;

    @Column(name = "resume_title")
    private String title;

    @Column(name = "resume_update_at")
    private LocalDate updateAt;

    @Column(name = "file_no")
    private Long fileNo;

}
