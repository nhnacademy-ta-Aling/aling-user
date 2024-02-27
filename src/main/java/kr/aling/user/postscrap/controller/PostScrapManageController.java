package kr.aling.user.postscrap.controller;

import kr.aling.user.postscrap.service.PostScrapManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게시물 스크랩 CUD RestController.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/post-scraps")
@RestController
public class PostScrapManageController {

    private final PostScrapManageService postScrapManageService;

    /**
     * 게시물을 스크랩합니다. 이미 스크랩한 게시물이라면 스크랩 취소합니다.
     *
     * @param postNo 스크랩할 게시물 번호
     * @param userNo 스크랩하는 회원의 번호
     * @return 200 OK
     * @author 이수정
     * @since 1.0
     */
    @PostMapping("/{postNo}")
    public ResponseEntity<Void> postScrap(@PathVariable Long postNo, @RequestParam Long userNo) {
        postScrapManageService.postScrap(postNo, userNo);
        return ResponseEntity.ok().build();
    }

}