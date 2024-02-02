package kr.aling.user.user.service.impl;

import javax.transaction.Transactional;
import kr.aling.user.user.dto.response.CreateUserResponseDto;
import kr.aling.user.user.dto.resquest.CreateUserRequestDto;
import kr.aling.user.user.entity.User;
import kr.aling.user.user.repository.UserManageRepository;
import kr.aling.user.user.service.UserManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 회원 CUD Service 구현체.
 *
 * @author : 이수정
 * @since : 1.0
 */
@RequiredArgsConstructor
@Transactional
@Service
public class UserManageServiceImpl implements UserManageService {

    private final PasswordEncoder passwordEncoder;

    private final UserManageRepository userManageRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateUserResponseDto registerUser(CreateUserRequestDto requestDto) {
        User user = User.builder()
                .id(requestDto.getId())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .build();
        user = userManageRepository.save(user);
        return new CreateUserResponseDto(user.getUserNo());
    }
}
