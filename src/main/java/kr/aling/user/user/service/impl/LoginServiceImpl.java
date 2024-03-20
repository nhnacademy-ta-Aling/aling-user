package kr.aling.user.user.service.impl;

import kr.aling.user.common.annotation.ReadService;
import kr.aling.user.user.dto.response.LoginInfoResponseDto;
import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.dto.resquest.LoginRequestDto;
import kr.aling.user.user.exception.UserNotFoundException;
import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 로그인 Service 구현체.
 *
 * @author 이수정
 * @since 1.0
 */
@ReadService
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserReadRepository userReadRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
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
