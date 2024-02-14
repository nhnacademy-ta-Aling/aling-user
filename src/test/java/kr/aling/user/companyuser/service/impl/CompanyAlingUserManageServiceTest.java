package kr.aling.user.companyuser.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.aling.user.companyuser.dto.request.CreateCompanyUserRequestDto;
import kr.aling.user.companyuser.dto.response.CreateCompanyUserResponseDto;
import kr.aling.user.companyuser.dummy.CompanyUserDummy;
import kr.aling.user.companyuser.entity.CompanyUser;
import kr.aling.user.companyuser.repository.CompanyUserManageRepository;
import kr.aling.user.companyuser.service.CompanyUserManageService;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import kr.aling.user.user.repository.UserManageRepository;
import kr.aling.user.user.repository.UserReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * The test class for CompanyUserManageService Implementation.
 *
 * @author : 여운석
 * @since : 1.0
 **/
class CompanyAlingUserManageServiceTest {
    CompanyUserManageService companyUserManageService;
    CompanyUserManageRepository companyUserManageRepository;
    UserManageRepository userManageRepository;
    UserReadRepository userReadRepository;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        companyUserManageRepository = Mockito.mock(CompanyUserManageRepository.class);
        userManageRepository = Mockito.mock(UserManageRepository.class);
        userReadRepository = Mockito.mock(UserReadRepository.class);

        passwordEncoder = new BCryptPasswordEncoder();
        companyUserManageService = new CompanyUserManageServiceImpl(userManageRepository, userReadRepository, companyUserManageRepository, passwordEncoder);
    }

    @Test
    @DisplayName("법인 회원가입 성공")
    void registerCompanyUser() {
        AlingUser alingUser = UserDummy.dummyEncoder(passwordEncoder);
        ReflectionTestUtils.setField(alingUser, "userNo", 10_000_000L);
        CompanyUser companyUser = CompanyUserDummy.dummy(alingUser);

        when(userManageRepository.save(any())).thenReturn(alingUser);
        when(companyUserManageRepository.save(any())).thenReturn(companyUser);
        when(userReadRepository.existsByEmail(anyString())).thenReturn(Boolean.FALSE);

        CreateCompanyUserRequestDto requestDto = new CreateCompanyUserRequestDto();
        ReflectionTestUtils.setField(requestDto, "email", alingUser.getId());
        ReflectionTestUtils.setField(requestDto, "password", "nhn123456");
        ReflectionTestUtils.setField(requestDto, "name", alingUser.getName());
        ReflectionTestUtils.setField(requestDto, "address", alingUser.getAddress());
        ReflectionTestUtils.setField(requestDto, "companyRegistrationNo", companyUser.getRegistrationNo());
        ReflectionTestUtils.setField(requestDto, "companySize", companyUser.getCompanySize());
        ReflectionTestUtils.setField(requestDto, "companySector", companyUser.getSector());

        CreateCompanyUserResponseDto expects = companyUserManageService.registerCompanyUser(requestDto);

        assertThat(expects.getName()).isEqualTo(companyUser.getAlingUser().getName());

        verify(userManageRepository, times(1))
                .save(any());
        verify(companyUserManageRepository, times(1))
                .save(any());
    }

    @Test
    @DisplayName("법인 회원가입 실패")
    void registerCompanyUserFailedExistEmail() {
        AlingUser alingUser = UserDummy.dummyEncoder(passwordEncoder);
        ReflectionTestUtils.setField(alingUser, "userNo", 10_000_000L);
        CompanyUser companyUser = CompanyUserDummy.dummy(alingUser);

        when(userManageRepository.save(any())).thenReturn(alingUser);
        when(companyUserManageRepository.save(any())).thenReturn(companyUser);
        when(userReadRepository.existsByEmail(anyString())).thenReturn(Boolean.TRUE);

        CreateCompanyUserRequestDto requestDto = new CreateCompanyUserRequestDto();
        ReflectionTestUtils.setField(requestDto, "email", alingUser.getId());
        ReflectionTestUtils.setField(requestDto, "password", "nhn123456");
        ReflectionTestUtils.setField(requestDto, "name", alingUser.getName());
        ReflectionTestUtils.setField(requestDto, "address", alingUser.getAddress());
        ReflectionTestUtils.setField(requestDto, "companyRegistrationNo", companyUser.getRegistrationNo());
        ReflectionTestUtils.setField(requestDto, "companySize", companyUser.getCompanySize());
        ReflectionTestUtils.setField(requestDto, "companySector", companyUser.getSector());

        assertThatThrownBy(() -> companyUserManageService.registerCompanyUser(requestDto))
                .isInstanceOf(UserEmailAlreadyUsedException.class);
    }
}