package kr.aling.user.postscrap.service;

import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.postscrap.dto.response.IsExistsPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.NumberOfPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsResponseDto;
import org.springframework.data.domain.Pageable;

/**
 * 게시물 스크랩 조회 Service interface.
 *
 * @author 이수정
 * @since 1.0
 */
public interface PostScrapReadService {

    /**
     * 스크랩한 게시물인지 확인합니다.
     *
     * @param postNo 확인할 게시물 번호
     * @param userNo 확인하는 회원의 번호
     * @return 회원의 게시물 스크랩 여부
     * @author 이수정
     * @since 1.0
     */
    IsExistsPostScrapResponseDto isExistsPostScrap(Long postNo, Long userNo);

    /**
     * 게시물 스크랩 횟수를 조회합니다.
     *
     * @param postNo 조회할 게시물 번호
     * @return 스크랩 횟수
     * @author 이수정
     * @since 1.0
     */
    NumberOfPostScrapResponseDto getNumberOfPostScrap(Long postNo);

    /**
     * 게시물 스크랩 페이징 조회해 해당하는 게시물을 조회합니다.
     *
     * @param userNo   조회하는 회원의 번호
     * @param pageable 페이징 정보를 담는 Pageable 객체
     * @return 페이징 조회된 게시물 스크랩의 게시물 목록
     * @author 이수정
     * @since 1.0
     */
    PageResponseDto<ReadPostScrapsResponseDto> getPostScraps(Long userNo, Pageable pageable);
}
