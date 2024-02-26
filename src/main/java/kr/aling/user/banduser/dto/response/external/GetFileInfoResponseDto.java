package kr.aling.user.banduser.dto.response.external;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 번호를 가지고 파일 서버에 조회 API 응답 Dto.
 *
 * @author 박경서
 * @since 1.0
 **/
@Getter
@NoArgsConstructor
public class GetFileInfoResponseDto {

    private Long fileNo;

    private Integer categoryNo;
    private String categoryName;

    private String path;
    private String originName;
    private String fileSize;
}
