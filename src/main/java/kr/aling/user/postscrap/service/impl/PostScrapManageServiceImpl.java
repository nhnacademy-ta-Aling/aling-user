package kr.aling.user.postscrap.service.impl;

import java.util.Objects;
import kr.aling.user.common.feign.PostFeignClient;
import kr.aling.user.post.exception.PostNotFoundException;
import kr.aling.user.postscrap.entity.PostScrap;
import kr.aling.user.postscrap.entity.PostScrap.Pk;
import kr.aling.user.postscrap.repository.PostScrapManageRepository;
import kr.aling.user.postscrap.repository.PostScrapReadRepository;
import kr.aling.user.postscrap.service.PostScrapManageService;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.service.UserReadService;
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

    private final PostScrapManageRepository postScrapManageRepository;
    private final PostScrapReadRepository postScrapReadRepository;

    private final UserReadService userReadService;

    private final PostFeignClient postFeignClient;

    /**
     * {@inheritDoc}
     *
     * @param authorization Access 토큰
     * @param postNo        스크랩할 게시물 번호
     * @param userNo        스크랩하는 회원의 번호
     */
    @Override
    public void postScrap(String authorization, Long postNo, Long userNo) {
        AlingUser alingUser = userReadService.getAlingUserByUserNo(userNo);

        if (Boolean.FALSE.equals(
                Objects.requireNonNull(postFeignClient.isExistsPost(authorization, postNo).getBody()).getIsExists())) {
            throw new PostNotFoundException(postNo);
        }

        Pk pk = new Pk(userNo, postNo);
        if (postScrapReadRepository.existsById(pk)) {
            postScrapManageRepository.deleteById(pk);
        } else {
            PostScrap postScrap = new PostScrap(pk, alingUser);
            postScrapManageRepository.save(postScrap);
        }
    }
}
