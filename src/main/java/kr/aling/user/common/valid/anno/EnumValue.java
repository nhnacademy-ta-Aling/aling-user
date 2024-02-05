package kr.aling.user.common.valid.anno;


import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import kr.aling.user.common.valid.validator.EnumValidator;

/**
 * Enum으로 관리하는 클래스들을 검증하기 위한 어노테이션입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidator.class)
public @interface EnumValue {

    /**
     * 검증할 Enum class.
     *
     * @return 지정한 enum class
     *
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 검증 실패시 나타나는 메시지.
     *
     * @return 메시지
     */
    String message() default "must be in enum constants";

    /**
     * 상황별 validation을 위한 메서드.
     *
     * @return class
     */
    Class<?>[] groups() default {};

    /**
     * 심각한 정도를 나타 냄.
     *
     * @return 심각한 정도
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 대소문자 구분 여부.
     *
     * @return 구분 여부
     */
    boolean ignoreCase() default false;
}
