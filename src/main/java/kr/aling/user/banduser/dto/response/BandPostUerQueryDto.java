package kr.aling.user.banduser.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * User 서버 회원 정보 조회 용 Dto.
 *
 * @author 박경서
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class BandPostUerQueryDto {

    private Long userNo;
    private String username;
    private Long fileNo;
}
