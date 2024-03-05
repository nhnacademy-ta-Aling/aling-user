package kr.aling.user.recruitpostscrap.service.impl;

import kr.aling.user.recruitpostscrap.service.RecruitPostScrapManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 채용공고 스크랩 CUD Service 구현체.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Transactional
@Service
public class RecruitPostScrapManageServiceImpl implements RecruitPostScrapManageService {

    /**
     * {@inheritDoc}
     *
     * @param recruitPostNo 스크랩할 채용공고 번호
     * @param userNo        스크랩하는 회원의 번호
     */
    @Override
    public void recruitPostScrap(Long recruitPostNo, Long userNo) {

    }
}
