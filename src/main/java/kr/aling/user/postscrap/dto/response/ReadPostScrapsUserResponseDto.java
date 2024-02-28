package kr.aling.user.postscrap.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시물 기준 게시물 스크랩 사용자를 조회한 상세 응답을 담는 Dto
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
public class ReadPostScrapsUserResponseDto {

    private Long userNo;
    private String name;
    private Boolean isDelete;
    private Long fileNo;
}
