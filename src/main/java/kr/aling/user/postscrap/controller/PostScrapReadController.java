package kr.aling.user.postscrap.controller;

import java.util.List;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.postscrap.dto.response.IsExistsPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.NumberOfPostScrapResponseDto;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsPostResponseDto;
import kr.aling.user.postscrap.dto.response.ReadPostScrapsUserResponseDto;
import kr.aling.user.postscrap.service.PostScrapReadService;
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
 * 게시물 스크랩 조회 RestController.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/post-scraps")
@RestController
public class PostScrapReadController {

    private final PostScrapReadService postScrapReadService;

    /**
     * 회원이 해당 게시물을 스크랩했는지 확인합니다.
     *
     * @param postNo 확인할 게시물 번호
     * @param userNo 확인하는 회원의 번호
     * @return 회원의 게시물 스크랩 여부
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/{postNo}")
    public ResponseEntity<IsExistsPostScrapResponseDto> isExistsPostScrap(@PathVariable Long postNo,
            @RequestParam Long userNo) {
        return ResponseEntity.ok(postScrapReadService.isExistsPostScrap(postNo, userNo));
    }

    /**
     * 게시물 스크랩 횟수를 조회합니다.
     *
     * @param postNo 스크랩 횟수 조회할 게시물의 번호
     * @return 게시물 스크랩 횟수
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/number")
    public ResponseEntity<NumberOfPostScrapResponseDto> getNumberOfPostScrap(@RequestParam Long postNo) {
        return ResponseEntity.ok(postScrapReadService.getNumberOfPostScrap(postNo));
    }

    /**
     * 마이페이지의 게시물 스크랩 탭에 사용될 게시물을 페이징 조회입니다.
     *
     * @param userNo   마이페이지 주인 회원 번호
     * @param pageable 페이징 정보를 담는 Pageable 객체
     * @return 페이징 조회된 게시물 스크랩의 게시물 목록
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/posts")
    public ResponseEntity<PageResponseDto<ReadPostScrapsPostResponseDto>> getPostScrapsPost(@RequestParam Long userNo,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(postScrapReadService.getPostScrapsPost(userNo, pageable));
    }

    /**
     * 게시물 기준으로 스크랩한 회원을 조회합니다.
     *
     * @param postNo 게시물 번호
     * @return 조회된 스크랩한 회원 목록
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/users")
    public ResponseEntity<List<ReadPostScrapsUserResponseDto>> getPostScrapsUser(@RequestParam Long postNo) {
        return ResponseEntity.ok(postScrapReadService.getPostScrapsUser(postNo));
    }

}