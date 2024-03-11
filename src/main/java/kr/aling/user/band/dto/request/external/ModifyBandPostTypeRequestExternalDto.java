package kr.aling.user.band.dto.request.external;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Some description here.
 *
 * @author 정유진
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class ModifyBandPostTypeRequestExternalDto {

    private Long bandNo;
    private String bandPostTypeName;
}
