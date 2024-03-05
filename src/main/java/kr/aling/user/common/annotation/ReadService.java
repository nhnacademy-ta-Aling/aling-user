package kr.aling.user.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * org.springframework 의 Service 어노테이션과 Transactional 어노테이션을 포함하는 어노테이션입니다. Service, Transactional 어노테이션의 반복을 줄이고
 * Transactional 에 대한 속성 누락을 방지할 수 있을 거 같습니다.
 *
 * @author : 이성준
 * @see Service
 * @see Transactional
 * @since 1.0
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(readOnly = true)
@Service
public @interface ReadService {

}
