package kr.aling.user.banduserrole.repository.impl;

import kr.aling.user.banduserrole.entity.BandUserRole;
import kr.aling.user.banduserrole.repository.BandUserRoleReadRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 그룹 회원 권한을 조회 하기 위한 Repository 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandUserRoleReadRepositoryImpl extends QuerydslRepositorySupport implements
        BandUserRoleReadRepositoryCustom {
    public BandUserRoleReadRepositoryImpl() {
        super(BandUserRole.class);
    }
}
