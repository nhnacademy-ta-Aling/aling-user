package kr.aling.user.banduser.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 그룹 회원의 그룹 회원 권한을 변경 하기 위한 정보를 담은 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyRoleOfBandUserRequestDto {
    @NotNull
    private Integer bandUserRoleNo;
}
