package kr.aling.user.user.service.impl;

import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.UserNotFoundException;
import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserReadServiceImpl implements UserReadService {

    private final UserReadRepository userReadRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean isExistsEmail(String email) {
        return userReadRepository.existsByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean isExistsUserNo(Long userNo) {
        return userReadRepository.existsById(userNo);
    }

    /**
     * {@inheritDoc}
     *
     * @param userNo 조회할 회원의 번호
     * @return 조회된 회원 Entity
     */
    @Transactional(readOnly = true)
    @Override
    public AlingUser getAlingUserByUserNo(Long userNo) {
        return userReadRepository.findById(userNo)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        LoginInfoResponseDto response = userReadRepository.findByEmailForLogin(loginRequestDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(loginRequestDto.getPassword(), response.getPassword())) {
            return new LoginResponseDto(response.getUserNo(),
                    userReadRepository.findRolesByUserNo(response.getUserNo()));
        }

        throw new UserNotFoundException();
    }
}
