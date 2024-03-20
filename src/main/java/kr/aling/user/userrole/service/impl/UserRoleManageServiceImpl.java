package kr.aling.user.userrole.service.impl;

import kr.aling.user.common.annotation.ManageService;
import kr.aling.user.role.entity.Role;
import kr.aling.user.role.service.RoleReadService;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.service.UserReadService;
import kr.aling.user.userrole.entity.UserRole;
import kr.aling.user.userrole.entity.UserRole.Pk;
import kr.aling.user.userrole.repository.UserRoleManageRepository;
import kr.aling.user.userrole.service.UserRoleManageService;
import lombok.RequiredArgsConstructor;

/**
 * 회원권한 CUD Service 구현체.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@ManageService
public class UserRoleManageServiceImpl implements UserRoleManageService {

    private final UserRoleManageRepository userRoleManageRepository;

    private final UserReadService userReadService;
    private final RoleReadService roleReadService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerDefaultUserRole(long userNo) {
        AlingUser user = userReadService.getAlingUserByUserNo(userNo);
        Role role = roleReadService.getRoleByName("ROLE_USER");

        UserRole userRole = new UserRole(new Pk(user.getUserNo(), role.getRoleNo()), user, role);
        userRoleManageRepository.save(userRole);
    }
}
