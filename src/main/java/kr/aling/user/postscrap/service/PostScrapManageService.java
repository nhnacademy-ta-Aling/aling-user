package kr.aling.user.postscrap.service;

/**
 * 게시물 스크랩 CUD Service interface.
 *
 * @author 이수정
 * @since 1.0
 */
public interface PostScrapManageService {

    /**
     * 게시물을 스크랩합니다. 이미 스크랩한 게시물이라면 게시물 스크랩을 취소합니다.
     *
     * @param authorization Access 토큰
     * @param postNo        스크랩할 게시물 번호
     * @param userNo        스크랩하는 회원의 번호
     * @author 이수정
     * @since 1.0
     */
    void postScrap(String authorization, Long postNo, Long userNo);
}
