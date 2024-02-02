package kr.aling.user.common.validator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import kr.aling.user.common.anno.EnumValue;

/**
 * Enum으로 관리하는 클래스들을 검증하기 위한 검증기입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class EnumValidator implements ConstraintValidator<EnumValue, String> {
    private List<String> enumValues;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        enumValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return enumValues.contains(value);
    }
}
