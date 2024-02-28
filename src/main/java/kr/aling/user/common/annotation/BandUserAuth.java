package kr.aling.user.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 그룹 회원 User 권한 부여를 위한 어노테이션. <br /> User 권한은 User 권한을 포함합니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BandUserAuth {

}
