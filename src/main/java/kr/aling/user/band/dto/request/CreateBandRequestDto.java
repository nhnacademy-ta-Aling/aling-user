package kr.aling.user.band.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 그룹 생성 요청 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoArgsConstructor
@Getter
public class CreateBandRequestDto {
    @NotBlank
    @Size(max = 30)
    private String bandName;

    @NotNull
    private Boolean isEnter;

    @NotNull
    private Boolean isViewContent;

    @NotBlank
    @Size(max = 1000)
    private String bandInfo;

    private Long fileNo;
}
