package kr.aling.user.band.repository.impl;

import static com.querydsl.core.group.GroupBy.groupBy;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.entity.Band;
import kr.aling.user.band.entity.QBand;
import kr.aling.user.band.repository.BandReadRepositoryCustom;
import kr.aling.user.banduser.entity.QBandUser;
import kr.aling.user.user.dto.response.GetBandInfoResponseDto;
import kr.aling.user.user.dto.response.GetBandUserInfoResponseDto;
import kr.aling.user.user.entity.QAlingUser;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetBandDetailInfoResponseDto> getBandDetailInfoByBandName(String bandName) {
        QBand band = QBand.band;
        QBandUser bandUser = QBandUser.bandUser;
        QAlingUser alingUser = QAlingUser.alingUser;

        List<GetBandDetailInfoResponseDto> result = from(band)
                .innerJoin(bandUser)
                .on(band.bandNo.eq(bandUser.band.bandNo))
                .innerJoin(alingUser)
                .on(bandUser.alingUser.userNo.eq(alingUser.userNo))
                .where(bandUser.isDelete.isFalse()
                        .and(band.name.eq(bandName)))
                .transform(groupBy(band.bandNo).list(Projections.constructor(GetBandDetailInfoResponseDto.class,
                        band.name,
                        band.fileNo,
                        band.info,
                        band.isEnter,
                        band.isViewContent,
                        band.isUpload,
                        GroupBy.list(Projections.constructor(GetBandUserInfoResponseDto.class,
                                alingUser.userNo,
                                alingUser.fileNo,
                                alingUser.name)))));

        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetBandInfoResponseDto> getSearchBandInfoListByBandName(String bandName) {
        QBand band = QBand.band;

        return from(band)
                .where(band.name.contains(bandName)
                        .and(band.isDelete.isFalse()))
                .select(Projections.constructor(GetBandInfoResponseDto.class,
                        band.bandNo,
                        band.name,
                        band.fileNo,
                        band.info))
                .fetch();
    }
}
