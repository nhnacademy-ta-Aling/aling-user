package kr.aling.user.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 그룹 회원 Creator 권한 부여를 위한 어노테이션. <br /> Creator 권한은 Creator, Admin, User 권한을 모두 포함합니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BandCreatorAuth {

}
