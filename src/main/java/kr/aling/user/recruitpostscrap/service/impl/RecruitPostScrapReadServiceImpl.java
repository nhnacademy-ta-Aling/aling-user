package kr.aling.user.recruitpostscrap.service.impl;

import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.IsExistsRecruitPostScrapResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.NumberOfRecruitPostScrapResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.ReadDDayRecruitPostScrapsResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.ReadRecruitPostScrapsResponseDto;
import kr.aling.user.recruitpostscrap.service.RecruitPostScrapReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 채용공고 스크랩 조회 Service 구현체.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecruitPostScrapReadServiceImpl implements RecruitPostScrapReadService {

    /**
     * {@inheritDoc}
     *
     * @param postNo 확인할 채용공고 번호
     * @param userNo 확인하는 회원의 번호
     * @return 회원의 채용공고 스크랩 여부
     */
    @Override
    public IsExistsRecruitPostScrapResponseDto isExistsRecruitPostScrap(Long postNo, Long userNo) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param postNo 조회할 채용공고 번호
     * @return 스크랩 횟수
     */
    @Override
    public NumberOfRecruitPostScrapResponseDto getNumberOfRecruitPostScrap(Long postNo) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param userNo   회원 번호
     * @param pageable 페이징 정보를 담는 Pageable 객체
     * @return 페이징 조회된 채용공고 D-Day 스크랩
     */
    @Override
    public PageResponseDto<ReadDDayRecruitPostScrapsResponseDto> getDDayRecruitPostScraps(Long userNo,
            Pageable pageable) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param userNo   조회하는 회원의 번호
     * @param pageable 페이징 정보를 담는 Pageable 객체
     * @return 페이징 조회된 채용공고 스크랩의 채용공고 목록
     */
    @Override
    public PageResponseDto<ReadRecruitPostScrapsResponseDto> getRecruitPostScraps(Long userNo, Pageable pageable) {
        return null;
    }
}
