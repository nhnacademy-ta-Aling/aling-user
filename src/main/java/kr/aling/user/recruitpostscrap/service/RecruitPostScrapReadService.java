package kr.aling.user.recruitpostscrap.service;

import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.IsExistsRecruitPostScrapResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.NumberOfRecruitPostScrapResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.ReadDDayRecruitPostScrapsResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.ReadRecruitPostScrapsResponseDto;
import org.springframework.data.domain.Pageable;

/**
 * 채용공고 스크랩 조회 Service interface.
 *
 * @author 이수정
 * @since 1.0
 */
public interface RecruitPostScrapReadService {

    /**
     * 스크랩한 채용공고인지 확인합니다.
     *
     * @param postNo 확인할 채용공고 번호
     * @param userNo 확인하는 회원의 번호
     * @return 회원의 채용공고 스크랩 여부
     * @author 이수정
     * @since 1.0
     */
    IsExistsRecruitPostScrapResponseDto isExistsRecruitPostScrap(Long postNo, Long userNo);

    /**
     * 채용공고 스크랩 횟수를 조회합니다.
     *
     * @param postNo 조회할 채용공고 번호
     * @return 스크랩 횟수
     * @author 이수정
     * @since 1.0
     */
    NumberOfRecruitPostScrapResponseDto getNumberOfRecruitPostScrap(Long postNo);

    /**
     * 채용공고 스크랩 중 최신순으로 정렬해 D-Day 페이징 조회합니다.
     *
     * @param userNo   회원 번호
     * @param pageable 페이징 정보를 담는 Pageable 객체
     * @return 페이징 조회된 채용공고 D-Day 스크랩
     * @author 이수정
     * @since 1.0
     */
    PageResponseDto<ReadDDayRecruitPostScrapsResponseDto> getDDayRecruitPostScraps(Long userNo, Pageable pageable);

    /**
     * 채용공고 스크랩 페이징 조회해 해당하는 채용공고를 조회합니다.
     *
     * @param userNo   조회하는 회원의 번호
     * @param pageable 페이징 정보를 담는 Pageable 객체
     * @return 페이징 조회된 채용공고 스크랩의 채용공고 목록
     * @author 이수정
     * @since 1.0
     */
    PageResponseDto<ReadRecruitPostScrapsResponseDto> getRecruitPostScraps(Long userNo, Pageable pageable);
}
