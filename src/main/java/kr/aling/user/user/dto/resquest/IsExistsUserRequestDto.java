package kr.aling.user.user.dto.resquest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 존재여부 확인 요청 파라미터를 담는 Dto.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IsExistsUserRequestDto {

    @NotNull
    @Positive
    private Long userNo;
}
