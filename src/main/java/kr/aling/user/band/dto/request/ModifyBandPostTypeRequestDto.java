package kr.aling.user.band.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Some description here.
 *
 * @author 정유진
 * @since 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyBandPostTypeRequestDto {
    @NotBlank
    @Size(max = 10)
    private String bandPostTypeName;
}
