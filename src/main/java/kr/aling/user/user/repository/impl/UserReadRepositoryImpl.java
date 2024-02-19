package kr.aling.user.user.repository.impl;


import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
import kr.aling.user.band.entity.QBand;
import kr.aling.user.banduser.entity.QBandUser;
import kr.aling.user.role.entity.QRole;
import kr.aling.user.user.dto.response.LoginInfoResponseDto;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.entity.QAlingUser;
import kr.aling.user.user.repository.UserReadRepositoryCustom;
import kr.aling.user.userrole.entity.QUserRole;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<LoginInfoResponseDto> findByEmailForLogin(String email) {
        QAlingUser user = QAlingUser.alingUser;

        return Optional.ofNullable(from(user)
                .select(Projections.constructor(
                        LoginInfoResponseDto.class,
                        user.userNo,
                        user.password
                )).where(user.id.eq(email))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> findRolesByUserNo(Long userNo) {
        QAlingUser user = QAlingUser.alingUser;
        QRole role = QRole.role;
        QUserRole userRole = QUserRole.userRole;

        return from(userRole)
                .select(role.name)
                .innerJoin(role).on(userRole.role.eq(role))
                .innerJoin(user).on(userRole.alingUser.eq(user))
                .where(user.userNo.eq(userNo))
                .fetch();
    }
}
