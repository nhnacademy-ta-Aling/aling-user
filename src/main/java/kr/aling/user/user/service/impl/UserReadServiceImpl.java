package kr.aling.user.user.service.impl;

import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 읽기 전용 서비스 구현체.
 *
 * @author : 여운석
 * @author : 이수정
 * @since : 1.0
 **/
@RequiredArgsConstructor
@Service
public class UserReadServiceImpl implements UserReadService {

    private final UserReadRepository userReadRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean existsEmail(String email) {
        return userReadRepository.existsByEmail(email);
    }
}
