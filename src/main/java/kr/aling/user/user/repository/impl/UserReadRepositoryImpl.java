package kr.aling.user.user.repository.impl;

import kr.aling.user.user.entity.QUser;
import kr.aling.user.user.entity.User;
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
        super(User.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isEmailExist(String email) {
        QUser user = QUser.user;

        return from(user).select(user.id).where(user.id.eq(email)).fetchCount() >= 1;
    }
}
