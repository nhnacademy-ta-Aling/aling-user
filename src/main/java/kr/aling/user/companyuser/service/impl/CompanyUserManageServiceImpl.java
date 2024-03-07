package kr.aling.user.companyuser.service.impl;

import kr.aling.user.companyuser.dto.request.CreateCompanyUserRequestDto;
import kr.aling.user.companyuser.dto.response.CreateCompanyUserResponseDto;
import kr.aling.user.companyuser.entity.CompanyUser;
import kr.aling.user.companyuser.repository.CompanyUserManageRepository;
import kr.aling.user.companyuser.service.CompanyUserManageService;
import kr.aling.user.role.entity.Role;
import kr.aling.user.role.repository.RoleReadRepository;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import kr.aling.user.user.repository.UserManageRepository;
import kr.aling.user.user.repository.UserReadRepository;
import kr.aling.user.userrole.entity.UserRole;
import kr.aling.user.userrole.repository.UserRoleManageRepository;
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
    private final UserRoleManageRepository userRoleManageRepository;
    private final RoleReadRepository roleReadRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ROLE_COMPANY = "ROLE_COMPANY";

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateCompanyUserResponseDto registerCompanyUser(CreateCompanyUserRequestDto requestDto) {
        if (Boolean.TRUE.equals(userReadRepository.existsByEmail(requestDto.getEmail()))) {
            throw new UserEmailAlreadyUsedException(requestDto.getEmail());
        }

        Role role = roleReadRepository.findByName(ROLE_COMPANY);

        AlingUser alingUser = AlingUser.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .address(requestDto.getAddress()).build();

        alingUser = userManageRepository.save(alingUser);

        CompanyUser companyUser = CompanyUser.builder()
                .userNo(alingUser.getUserNo())
                .alingUser(alingUser)
                .registrationNo(requestDto.getCompanyRegistrationNo())
                .companySize(requestDto.getCompanySize())
                .sector(requestDto.getCompanySector())
                .build();

        companyUserManageRepository.save(companyUser);

        UserRole userRole = new UserRole(new UserRole.Pk(alingUser.getUserNo(), role.getRoleNo()), alingUser, role);
        userRoleManageRepository.save(userRole);

        return new CreateCompanyUserResponseDto(companyUser.getAlingUser().getName());
    }
}
