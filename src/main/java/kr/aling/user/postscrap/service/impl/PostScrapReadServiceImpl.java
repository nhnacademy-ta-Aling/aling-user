package kr.aling.user.postscrap.service.impl;

import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.postscrap.dto.response.IsExistsPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.NumberOfPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsResponseDto;
import kr.aling.user.postscrap.service.PostScrapReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시물 스크랩 조회 Service 구현체.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostScrapReadServiceImpl implements PostScrapReadService {

    /**
     * {@inheritDoc}
     *
     * @param postNo 확인할 게시물 번호
     * @param userNo 확인하는 회원의 번호
     * @return 게시물 스크랩 여부
     */
    @Override
    public IsExistsPostScrapResponseDto isExistsPostScrap(Long postNo, Long userNo) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param postNo 조회할 게시물 번호
     * @return 게시물 스크랩 횟수
     */
    @Override
    public NumberOfPostScrapResponseDto getNumberOfPostScrap(Long postNo) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param userNo   조회하는 회원의 번호
     * @param pageable 페이징 정보를 담는 Pageable 객체
     * @return 페이징 조회된 게시물 스크랩의 게시물 목록
     */
    @Override
    public PageResponseDto<ReadPostScrapsResponseDto> getPostScraps(Long userNo, Pageable pageable) {
        return null;
    }
}
