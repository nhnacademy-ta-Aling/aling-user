package kr.aling.user.coverletter.entity;

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
 * 자기소개서 엔티티입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "cover_letter")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoverLetter {
    @Id
    @Column
    private Long coverLetterNo;

    @ManyToOne
    @JoinColumn(name = "normal_user_no")
    private NormalUser normalUser;

    @Column(name = "cover_letter_title")
    private String title;

    @Column(name = "cover_letter_content")
    private String content;

}
