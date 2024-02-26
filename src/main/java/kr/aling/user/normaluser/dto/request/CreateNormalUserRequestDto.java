package kr.aling.user.normaluser.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일반 회원가입 요청 파라미터를 담는 Dto.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateNormalUserRequestDto {

    @NotBlank
    @Size(min = 3, max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    @Positive
    private Integer wantJobTypeNo;

    @NotBlank
    @Size(min = 9, max = 11)
    @Pattern(regexp = "\\d{9,11}")
    private String phoneNo;

    @NotBlank
    @Size(min = 8, max = 8)
    @Pattern(regexp = "\\d{8}")
    private String birth;
}
