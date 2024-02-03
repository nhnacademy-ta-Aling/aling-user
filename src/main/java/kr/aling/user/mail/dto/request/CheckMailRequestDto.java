package kr.aling.user.mail.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이메일 인증 요청 파라미터를 담는 Dto.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckMailRequestDto {

    @NotBlank
    @Size(min = 3, max = 100)
    @Email
    private String email;
}
