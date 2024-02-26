package kr.aling.user.recruitpostscrap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 채용공고 스크랩 CUD RestController.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit-post-scraps")
@RestController
public class RecruitPostScrapManageController {

    private final RecruitPostScrapManageService recruitPostScrapManageService;

    /**
     * 채용공고를 스크랩합니다. 이미 스크랩한 채용공고라면 스크랩 취소합니다.
     *
     * @param recruitPostNo 스크랩할 채용공고 번호
     * @param userNo        스크랩하는 회원의 번호
     * @return 200 OK
     * @author 이수정
     * @since 1.0
     */
    @PostMapping("/{recruitPostNo}")
    public ResponseEntity<Void> recruitPostScrap(@PathVariable Long recruitPostNo, @RequestParam Long userNo) {
        recruitPostScrapManageService.recruitPostScrap(recruitPostNo, userNo);
        return ResponseEntity.ok().build();
    }
}
