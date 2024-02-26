package kr.aling.user.postscrap.service.impl;

import kr.aling.user.postscrap.service.PostScrapManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 게시물 스크랩 CUD Service 구현체.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Transactional
@Service
public class PostScrapManageServiceImpl implements PostScrapManageService {

    /**
     * {@inheritDoc}
     *
     * @param postNo 스크랩할 게시물 번호
     * @param userNo 스크랩하는 회원의 번호
     */
    @Override
    public void postScrap(Long postNo, Long userNo) {

    }
}
