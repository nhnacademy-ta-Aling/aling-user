package kr.aling.user.post.dto.response;

import java.util.List;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 스크랩 조회용 게시물을 조회하고 받은 응답 Dto.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class ReadPostsForScrapResponseDto {

    private List<ReadPostScrapsResponseDto> infos;
}

