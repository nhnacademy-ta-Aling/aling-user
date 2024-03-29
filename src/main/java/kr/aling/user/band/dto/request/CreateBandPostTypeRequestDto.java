package kr.aling.user.band.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 그룹 게시글 분류 요청 dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateBandPostTypeRequestDto {

    @NotBlank
    @Size(max = 10)
    private String name;
}
