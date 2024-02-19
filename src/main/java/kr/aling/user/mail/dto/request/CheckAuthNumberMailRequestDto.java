package kr.aling.user.mail.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 메일 인증번호 검증 요청 파라미터를 담는 Dto.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckAuthNumberMailRequestDto {

    @NotBlank
    @Size(min = 3, max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 6)
    private String authNumber;
}
