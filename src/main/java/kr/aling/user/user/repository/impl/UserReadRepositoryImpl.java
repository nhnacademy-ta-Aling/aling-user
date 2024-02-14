package kr.aling.user.user.repository.impl;

import com.querydsl.core.types.Projections;
import java.util.List;
import kr.aling.user.band.entity.QBand;
import kr.aling.user.banduser.entity.QBandUser;
import kr.aling.user.user.dto.response.GetBandInfoResponseDto;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetBandInfoResponseDto> getJoinedBandInfoListByUserNo(Long userNo) {
        QAlingUser alingUser = QAlingUser.alingUser;
        QBandUser bandUser = QBandUser.bandUser;
        QBand band = QBand.band;

        return from(alingUser)
                .where(alingUser.userNo.eq(userNo))
                .where(band.isDelete.isFalse())
                .innerJoin(bandUser)
                .on(alingUser.userNo.eq(bandUser.alingUser.userNo))
                .innerJoin(bandUser.band, band)
                .select(Projections.constructor(GetBandInfoResponseDto.class,
                        band.bandNo,
                        band.name,
                        band.fileNo,
                        band.info
                ))
                .fetch();
    }
}
