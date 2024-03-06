package kr.aling.user.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 게이트웨이에서 JWT를 파싱하고 헤더로 전달된 유저의 정보를 얻어 요청의 파라미터로 주입합니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectUserInfo {

}
