package kr.aling.user.recruitpostscrap.service;

/**
 * 채용공고 스크랩 CUD Service interface.
 *
 * @author 이수정
 * @since 1.0
 */
public interface RecruitPostScrapManageService {

    /**
     * 채용공고를 스크랩합니다. 이미 스크랩한 채용공고라면 스크랩을 취소합니다.
     *
     * @param recruitPostNo 스크랩할 채용공고 번호
     * @param userNo        스크랩하는 회원의 번호
     * @author 이수정
     * @since 1.0
     */
    void recruitPostScrap(Long recruitPostNo, Long userNo);
}
