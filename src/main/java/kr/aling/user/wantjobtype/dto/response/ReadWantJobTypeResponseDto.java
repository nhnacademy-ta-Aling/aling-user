package kr.aling.user.wantjobtype.dto.response;

import kr.aling.user.wantjobtype.entity.WantJobType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 조회된 WantJobType 객체를 담는 Dto.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
public class ReadWantJobTypeResponseDto {
    private final WantJobType wantJobType;
}
