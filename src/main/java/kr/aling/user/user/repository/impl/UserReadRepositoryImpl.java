package kr.aling.user.user.repository.impl;

import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.entity.QAlingUser;
import kr.aling.user.user.repository.UserReadRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * QueryDsl 사용을 위한 회원 읽기 전용 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class UserReadRepositoryImpl extends QuerydslRepositorySupport implements UserReadRepositoryCustom {

    public UserReadRepositoryImpl() {
        super(AlingUser.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean existsByEmail(String email) {
        QAlingUser alingUser = QAlingUser.alingUser;

        return from(alingUser).select(alingUser.id).where(alingUser.id.eq(email)).fetchCount() >= 1;
    }
}
