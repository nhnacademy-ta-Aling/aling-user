package kr.aling.user.band.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import java.util.List;
import java.util.Optional;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.entity.Band;
import kr.aling.user.band.entity.QBand;
import kr.aling.user.band.repository.BandReadRepositoryCustom;
import kr.aling.user.banduser.entity.QBandUser;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
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
    public Optional<GetBandDetailInfoResponseDto> getBandDetailInfoByBandName(String bandName, Long userNo) {
        QBand band = QBand.band;
        QBandUser bandUser = QBandUser.bandUser;

        return Optional.ofNullable(from(band)
                .where(band.isDelete.isFalse()
                        .and(band.name.eq(bandName)))
                .select(Projections.constructor(GetBandDetailInfoResponseDto.class,
                        band.bandNo,
                        band.name,
                        band.fileNo,
                        band.info,
                        band.isEnter,
                        band.isViewContent,
                        band.isUpload,
                        JPAExpressions.select(bandUser.bandUserNo)
                                .from(bandUser)
                                .innerJoin(bandUser.band, band)
                                .where(band.name.eq(bandName)
                                        .and(bandUser.alingUser.userNo.eq(userNo))
                                        .and(bandUser.isDelete.isFalse())
                                        .and(bandUser.isBlock.isFalse()))))
                .fetchOne());
    }

    /**
     * 그룹 명을 통해 그룹 목록을 조회 하는 메서드.
     *
     * @param bandName 그룹 명
     * @return 그룹 정보 리스트
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
                        band.info,
                        band.isEnter,
                        band.isViewContent,
                        band.isUpload))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetBandInfoResponseDto> getJoinedBandInfoListByUserNo(Long userNo) {
        QBandUser bandUser = QBandUser.bandUser;
        QAlingUser alingUser = QAlingUser.alingUser;
        QBand band = QBand.band;

        return from(bandUser)
                .innerJoin(bandUser.band, band)
                .innerJoin(bandUser.alingUser, alingUser)
                .where(alingUser.userNo.eq(userNo)
                        .and(bandUser.isDelete.isFalse())
                        .and(bandUser.isBlock.isFalse())
                        .and(band.isDelete.isFalse()))
                .select(Projections.constructor(GetBandInfoResponseDto.class,
                        band.bandNo,
                        band.name,
                        band.fileNo,
                        band.info,
                        band.isEnter,
                        band.isViewContent,
                        band.isUpload
                ))
                .fetch();
    }
}
