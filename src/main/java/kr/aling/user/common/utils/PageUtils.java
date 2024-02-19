package kr.aling.user.common.utils;

import kr.aling.user.common.dto.PageResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * 페이징 관련 유틸 class.
 *
 * @author : 정유진
 * @since : 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageUtils {

    /**
     * Page를 PageResponseDto로 변환합니다.
     *
     * @param page 변환할 Page 객체
     * @return 변환한 PageResponseDto 객체
     * @param <T> 페이징 내용 타입
     * @author : 이수정
     * @since : 1.0
     */
    public static <T> PageResponseDto<T> convert(Page<T> page) {
        return new PageResponseDto<>(
                page.getPageable().getPageNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent()
        );
    }
}
