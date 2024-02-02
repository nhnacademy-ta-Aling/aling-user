package kr.aling.user.band.repository.impl;

import kr.aling.user.band.entity.Band;
import kr.aling.user.band.repository.BandManageRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Some description here.
 *
 * @author 정유진
 * @since 1.0
 **/
public class BandManageRepositoryImpl extends QuerydslRepositorySupport implements BandManageRepositoryCustom {

    public BandManageRepositoryImpl() {
        super(Band.class);
    }

}
