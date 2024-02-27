package kr.aling.user.post.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시물 번호 리스트를 받아 게시물을 조회하기 위한 요청 Dto.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadPostsForScrapRequestDto {

    @NotNull
    private List<Long> postNos;
}
