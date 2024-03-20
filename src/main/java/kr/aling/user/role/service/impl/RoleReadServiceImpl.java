package kr.aling.user.role.service.impl;

import kr.aling.user.common.annotation.ReadService;
import kr.aling.user.role.entity.Role;
import kr.aling.user.role.repository.RoleReadRepository;
import kr.aling.user.role.service.RoleReadService;
import lombok.RequiredArgsConstructor;

/**
 * 권한 조회 Service 구현체.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@ReadService
public class RoleReadServiceImpl implements RoleReadService {

    private final RoleReadRepository roleReadRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Role getRoleByName(String name) {
        return roleReadRepository.findByName(name);
    }
}
