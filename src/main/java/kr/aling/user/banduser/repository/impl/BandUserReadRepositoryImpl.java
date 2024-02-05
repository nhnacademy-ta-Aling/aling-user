package kr.aling.user.banduser.repository.impl;

import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduser.repository.BandUserReadRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 그룹 회원을 조회 하기 위한 Repository 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandUserReadRepositoryImpl extends QuerydslRepositorySupport implements BandUserReadRepositoryCustom {
    public BandUserReadRepositoryImpl() {
        super(BandUser.class);
    }


}
