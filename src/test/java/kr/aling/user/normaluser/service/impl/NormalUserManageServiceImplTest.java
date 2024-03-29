package kr.aling.user.normaluser.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.format.DateTimeFormatter;
import kr.aling.user.normaluser.dto.request.CreateNormalUserRequestDto;
import kr.aling.user.normaluser.dummy.NormalUserDummy;
import kr.aling.user.normaluser.entity.NormalUser;
import kr.aling.user.normaluser.repository.NormalUserManageRepository;
import kr.aling.user.normaluser.service.NormalUserManageService;
import kr.aling.user.user.dto.response.CreateUserResponseDto;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import kr.aling.user.user.service.UserManageService;
import kr.aling.user.userrole.service.UserRoleManageService;
import kr.aling.user.wantjobtype.dto.response.ReadWantJobTypeResponseDto;
import kr.aling.user.wantjobtype.dummy.WantJobTypeDummy;
import kr.aling.user.wantjobtype.entity.WantJobType;
import kr.aling.user.wantjobtype.service.WantJobTypeReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class NormalUserManageServiceImplTest {

    public static final String BIRTH_PATTERN = "yyyyMMdd";
    private final String TMP_PASSWORD = "########";

    private NormalUserManageService normalUserManageService;

    private NormalUserManageRepository normalUserManageRepository;

    private UserManageService userManageService;
    private WantJobTypeReadService wantJobTypeReadService;

    private UserRoleManageService userRoleManageService;

    @BeforeEach
    void setUp() {
        normalUserManageRepository = mock(NormalUserManageRepository.class);

        userManageService = mock(UserManageService.class);
        wantJobTypeReadService = mock(WantJobTypeReadService.class);

        userRoleManageService = mock(UserRoleManageService.class);

        normalUserManageService = new NormalUserManageServiceImpl(
                normalUserManageRepository,
                userManageService,
                wantJobTypeReadService,
                userRoleManageService
        );
    }

    @Test
    @DisplayName("일반회원 등록 성공")
    void registerNormalUser() {
        // given
        AlingUser alingUser = UserDummy.dummyEncoder(new BCryptPasswordEncoder());
        WantJobType wantJobType = WantJobTypeDummy.dummy();
        NormalUser normalUser = NormalUserDummy.dummy(alingUser, wantJobType);

        CreateNormalUserRequestDto requestDto = new CreateNormalUserRequestDto(
                alingUser.getEmail(), TMP_PASSWORD, alingUser.getName(), normalUser.getWantJobType().getWantJobTypeNo(),
                normalUser.getPhoneNo(), normalUser.getBirth().format(DateTimeFormatter.ofPattern(BIRTH_PATTERN))
        );

        when(userManageService.registerUser(any())).thenReturn(new CreateUserResponseDto(1L));
        when(wantJobTypeReadService.findByWantJobTypeNo(any())).thenReturn(new ReadWantJobTypeResponseDto(wantJobType));
        when(normalUserManageRepository.save(any())).thenReturn(normalUser);
        doNothing().when(userRoleManageService).registerDefaultUserRole(anyLong());

        // when
        normalUserManageService.registerNormalUser(requestDto);

        // then
        verify(userManageService, times(1)).registerUser(any());
        verify(wantJobTypeReadService, times(1)).findByWantJobTypeNo(any());
        verify(normalUserManageRepository, times(1)).save(any());
        verify(userRoleManageService, times(1)).registerDefaultUserRole(anyLong());
    }

    @Test
    @DisplayName("일반회원 등록 실패 - 이미 존재하는 ID(Email)인 경우")
    void registerNormalUser_alreadyExistsEmail() {
        // given
        AlingUser alingUser = UserDummy.dummyEncoder(new BCryptPasswordEncoder());
        WantJobType wantJobType = WantJobTypeDummy.dummy();
        NormalUser normalUser = NormalUserDummy.dummy(alingUser, wantJobType);

        CreateNormalUserRequestDto requestDto = new CreateNormalUserRequestDto(
                alingUser.getEmail(), TMP_PASSWORD, alingUser.getName(), normalUser.getWantJobType().getWantJobTypeNo(),
                normalUser.getPhoneNo(), normalUser.getBirth().format(DateTimeFormatter.ofPattern(BIRTH_PATTERN))
        );

        when(userManageService.registerUser(any())).thenThrow(new UserEmailAlreadyUsedException(alingUser.getEmail()));

        // when
        // then
        assertThatThrownBy(() -> normalUserManageService.registerNormalUser(requestDto))
                .isInstanceOf(UserEmailAlreadyUsedException.class);

        verify(userManageService, times(1)).registerUser(any());
        verify(wantJobTypeReadService, never()).findByWantJobTypeNo(any());
        verify(normalUserManageRepository, never()).save(any());
    }
}