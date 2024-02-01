package kr.aling.user.techskill.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Some description here.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Entity
@Getter
@Table(name = "tech_skill")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechSkill {
    @Id
    @Column
    private Long techSkillNo;

    @Column(name = "tech_skill_name")
    private String name;

    @Column(name = "file_no")
    private Long fileNo;

}
