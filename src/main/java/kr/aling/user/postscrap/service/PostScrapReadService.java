package kr.aling.user.postscrap.service;

import java.util.List;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.postscrap.dto.response.IsExistsPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.NumberOfPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsPostResponseDto;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsUserResponseDto;
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
    PageResponseDto<ReadPostScrapsPostResponseDto> getPostScrapsPost(Long userNo, Pageable pageable);

    /**
     * 게시물 번호 기준으로 스크랩한 회원을 조회합니다.
     *
     * @param postNo 게시물 번호
     * @return 게시물 번호로 조회된 스크랩한 회원 리스트
     * @author 이수정
     * @since 1.0
     */
    List<ReadPostScrapsUserResponseDto> getPostScrapsUser(Long postNo);
}
