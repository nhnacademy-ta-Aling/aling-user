package kr.aling.user.companyuser.dto.request;

import javax.validation.constraints.Email;
import kr.aling.user.common.anno.EnumValue;
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
public class CompanyUserRegisterRequestDto {

    @Email
    private String email;

    @Length(min = 8, max = 20)
    private String password;

    @Length(min = 1, max = 50)
    private String name;

    @Length(min = 1, max = 200)
    private String address;

    @Length(min = 10, max = 10)
    private String companyRegistrationNo;

    @EnumValue(enumClass = CompanySize.class)
    private String companySize;

    @Length(max = 50)
    private String companySector;
}
