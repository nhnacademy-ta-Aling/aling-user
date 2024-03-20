package kr.aling.user.user.service.impl;

import kr.aling.user.common.annotation.ReadService;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.UserNotFoundException;
import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.user.service.UserReadService;
import lombok.RequiredArgsConstructor;

/**
 * 회원 읽기 읽기 Service 구현체.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@ReadService
public class UserReadServiceImpl implements UserReadService {

    private final UserReadRepository userReadRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExistsEmail(String email) {
        return userReadRepository.existsByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExistsUserNo(Long userNo) {
        return userReadRepository.existsById(userNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AlingUser getAlingUserByUserNo(Long userNo) {
        return userReadRepository.findById(userNo).orElseThrow(UserNotFoundException::new);
    }
}
