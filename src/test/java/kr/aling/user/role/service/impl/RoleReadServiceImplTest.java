package kr.aling.user.role.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.aling.user.role.entity.Role;
import kr.aling.user.role.repository.RoleReadRepository;
import kr.aling.user.role.service.RoleReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleReadServiceImplTest {

    private RoleReadService roleReadService;

    private RoleReadRepository roleReadRepository;

    @BeforeEach
    void setUp() {
        roleReadRepository = mock(RoleReadRepository.class);

        roleReadService = new RoleReadServiceImpl(roleReadRepository);
    }

    @Test
    @DisplayName("역할 이름으로 역할 엔티티 조회")
    void getRoleByName() {
        // given
        String name = "ROLE_USER";
        Role role = new Role(1, name);

        when(roleReadRepository.findByName(anyString())).thenReturn(role);

        // when
        Role result = roleReadService.getRoleByName(name);

        // then
        assertEquals(result.getRoleNo(), role.getRoleNo());
        assertEquals(result.getName(), name);
        verify(roleReadRepository, times(1)).findByName(anyString());
    }
}