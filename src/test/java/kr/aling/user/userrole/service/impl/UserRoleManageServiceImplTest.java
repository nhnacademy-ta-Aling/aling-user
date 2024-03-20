package kr.aling.user.userrole.service.impl;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.aling.user.role.entity.Role;
import kr.aling.user.role.service.RoleReadService;
import kr.aling.user.user.dummy.UserDummy;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.service.UserReadService;
import kr.aling.user.userrole.repository.UserRoleManageRepository;
import kr.aling.user.userrole.service.UserRoleManageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRoleManageServiceImplTest {

    private UserRoleManageService userRoleManageService;

    private UserRoleManageRepository userRoleManageRepository;

    private UserReadService userReadService;
    private RoleReadService roleReadService;

    @BeforeEach
    void setUp() {
        userRoleManageRepository = mock(UserRoleManageRepository.class);

        userReadService = mock(UserReadService.class);
        roleReadService = mock(RoleReadService.class);

        userRoleManageService = new UserRoleManageServiceImpl(
                userRoleManageRepository,
                userReadService,
                roleReadService
        );
    }

    @Test
    @DisplayName("회원 기본 권한 설정")
    void registerDefaultUserRole() {
        // given
        long userNo = 1L;
        String name = "ROLE_USER";

        AlingUser alingUserDummy = UserDummy.dummy();
        Role role = new Role(1, name);

        when(userReadService.getAlingUserByUserNo(anyLong())).thenReturn(alingUserDummy);
        when(roleReadService.getRoleByName(anyString())).thenReturn(role);

        // when
        userRoleManageService.registerDefaultUserRole(userNo);

        // then
        verify(userReadService, times(1)).getAlingUserByUserNo(anyLong());
        verify(roleReadService, times(1)).getRoleByName(anyString());
    }
}