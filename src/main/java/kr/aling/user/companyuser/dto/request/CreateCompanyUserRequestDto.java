package kr.aling.user.companyuser.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import kr.aling.user.common.valid.anno.EnumValue;
import kr.aling.user.companyuser.type.CompanySize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * The class CompanyUserRegisterRequestDto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoArgsConstructor
@Getter
public class CreateCompanyUserRequestDto {

    @Email
    private String email;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    @NotBlank
    @Length(min = 1, max = 50)
    private String name;

    @NotBlank
    @Length(min = 1, max = 200)
    private String address;

    @NotBlank
    @Length(min = 10, max = 10)
    private String companyRegistrationNo;

    @EnumValue(enumClass = CompanySize.class, message = "기업규모 검증 실패")
    private String companySize;

    @NotBlank
    @Length(max = 50)
    private String companySector;
}
