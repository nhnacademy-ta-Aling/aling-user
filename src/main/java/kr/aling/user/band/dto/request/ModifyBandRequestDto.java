package kr.aling.user.band.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 그룹 정보 수정 요청 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyBandRequestDto {

    @NotBlank
    @Size(max = 30)
    private String newBandName;

    @NotNull
    private Boolean isEnter;

    @NotNull
    private Boolean isViewContent;

    @Size(max = 1000)
    private String bandInfo;

    private Long fileNo;
}
