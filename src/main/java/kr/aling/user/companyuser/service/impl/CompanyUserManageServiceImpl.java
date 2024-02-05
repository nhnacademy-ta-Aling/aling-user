package kr.aling.user.companyuser.service.impl;

import kr.aling.user.companyuser.dto.request.CreateCompanyUserRequestDto;
import kr.aling.user.companyuser.dto.response.CreateCompanyUserResponseDto;
import kr.aling.user.companyuser.entity.CompanyUser;
import kr.aling.user.companyuser.repository.CompanyUserManageRepository;
import kr.aling.user.companyuser.service.CompanyUserManageService;
import kr.aling.user.user.entity.User;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import kr.aling.user.user.repository.UserManageRepository;
import kr.aling.user.user.repository.UserReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The class implementation of CompanyUserManageService.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional
public class CompanyUserManageServiceImpl implements CompanyUserManageService {

    private final UserManageRepository userManageRepository;
    private final UserReadRepository userReadRepository;
    private final CompanyUserManageRepository companyUserManageRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateCompanyUserResponseDto registerCompanyUser(CreateCompanyUserRequestDto requestDto) {
        if (Boolean.TRUE.equals(userReadRepository.existsByEmail(requestDto.getEmail()))) {
            throw new UserEmailAlreadyUsedException(requestDto.getEmail());
        }

        User user = User.builder()
                .id(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .address(requestDto.getAddress()).build();

        user = userManageRepository.save(user);

        CompanyUser companyUser = CompanyUser.builder()
                .user(user)
                .registrationNo(requestDto.getCompanyRegistrationNo())
                .companySize(requestDto.getCompanySize())
                .sector(requestDto.getCompanySector())
                .build();

        companyUserManageRepository.save(companyUser);

        return new CreateCompanyUserResponseDto(companyUser.getUser().getName());
    }
}
