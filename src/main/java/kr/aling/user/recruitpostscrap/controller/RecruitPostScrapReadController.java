package kr.aling.user.recruitpostscrap.controller;

import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.IsExistsRecruitPostScrapResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.NumberOfRecruitPostScrapResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.ReadDDayRecruitPostScrapsResponseDto;
import kr.aling.user.recruitpostscrap.dto.response.ReadRecruitPostScrapsResponseDto;
import kr.aling.user.recruitpostscrap.service.RecruitPostScrapReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 채용공고 스크랩 조회 RestController.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit-post-scraps")
@RestController
public class RecruitPostScrapReadController {

    private final RecruitPostScrapReadService recruitPostScrapReadService;

    /**
     * 회원이 해당 채용공고를 스크랩했는지 확인합니다.
     *
     * @param recruitPostNo 확인할 채용공고 번호
     * @param userNo        확인하는 회원의 번호
     * @return 회원의 채용공고 스크랩 여부
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/{recruitPostNo}")
    public ResponseEntity<IsExistsRecruitPostScrapResponseDto> isExistsRecruitPostScrap(
            @PathVariable Long recruitPostNo, @RequestParam Long userNo) {
        return ResponseEntity.ok(recruitPostScrapReadService.isExistsRecruitPostScrap(recruitPostNo, userNo));
    }

    /**
     * 채용공고 스크랩 횟수를 조회합니다.
     *
     * @param recruitPostNo 스크랩 횟수 조회할 채용공고의 번호
     * @return 채용공고 스크랩 횟수
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/{recruitPostNo}/number")
    public ResponseEntity<NumberOfRecruitPostScrapResponseDto> getNumberOfRecruitPostScrap(
            @PathVariable Long recruitPostNo) {
        return ResponseEntity.ok(recruitPostScrapReadService.getNumberOfRecruitPostScrap(recruitPostNo));
    }

    /**
     * 특정 회원의 채용공고 스크랩 중 최신순으로 정렬하여 D-Day를 포함해 페이징 조회합니다.
     *
     * @param userNo   회원 번호
     * @param pageable 페이징 정보를 담는 Pageable 객체
     * @return 페이징 조회된 채용공고 D-Day 스크랩
     */
    @GetMapping("/d-day")
    public ResponseEntity<PageResponseDto<ReadDDayRecruitPostScrapsResponseDto>> getDDayRecruitPostScraps(
            @RequestParam Long userNo, @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(recruitPostScrapReadService.getDDayRecruitPostScraps(userNo, pageable));
    }

    /**
     * 마이페이지의 채용공고 스크랩 탭에 사용될 페이징 조회입니다.
     *
     * @param userNo   마이페이지 주인 회원 번호
     * @param pageable 페이징 정보를 담는 Pageable 객체
     * @return 페이징 조회된 채용공고 스크랩의 게시물 목록
     * @author 이수정
     * @since 1.0
     */
    @GetMapping
    public ResponseEntity<PageResponseDto<ReadRecruitPostScrapsResponseDto>> getRecruitPostScraps(
            @RequestParam Long userNo, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(recruitPostScrapReadService.getRecruitPostScraps(userNo, pageable));
    }

}
