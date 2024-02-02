package kr.aling.user.band.repository.impl;

import kr.aling.user.band.entity.Band;
import kr.aling.user.band.repository.BandReadRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 그룹(Band)을 조회 하는 Repository 구현.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandReadRepositoryImpl extends QuerydslRepositorySupport implements BandReadRepositoryCustom {

    public BandReadRepositoryImpl() {
        super(Band.class);
    }
}
