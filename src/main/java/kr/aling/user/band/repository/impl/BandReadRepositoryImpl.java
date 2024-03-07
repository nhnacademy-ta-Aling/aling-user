package kr.aling.user.band.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
import kr.aling.user.band.entity.Band;
import kr.aling.user.band.entity.QBand;
import kr.aling.user.band.repository.BandReadRepositoryCustom;
import kr.aling.user.banduser.entity.QBandUser;
import kr.aling.user.user.entity.QAlingUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

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
     *
     * @param bandName 그룹 명
     * @return 그룹 정보 응답 dto
     */
    @Override
    public Optional<GetBandInfoResponseDto> getBandDetailInfoByBandName(String bandName) {
        QBand band = QBand.band;

        return Optional.ofNullable(from(band)
                .where(band.isDelete.isFalse()
                        .and(band.name.eq(bandName)))
                .select(Projections.constructor(GetBandInfoResponseDto.class,
                        band.bandNo,
                        band.name,
                        band.fileNo,
                        band.info,
                        band.isEnter,
                        band.isViewContent,
                        band.isUpload))
                .fetchOne());


    }

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹 명
     * @param pageable pageable
     * @return 그룹 정보 dto 페이지
     */
    @Override
    public Page<GetBandInfoResponseDto> getSearchBandInfoListByBandName(String bandName, Pageable pageable) {
        QBand band = QBand.band;

        List<GetBandInfoResponseDto> bandList = from(band)
                .where(band.name.contains(bandName)
                        .and(band.isDelete.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(Projections.constructor(GetBandInfoResponseDto.class,
                        band.bandNo,
                        band.name,
                        band.fileNo,
                        band.info,
                        band.isEnter,
                        band.isViewContent,
                        band.isUpload))
                .fetch();

        JPQLQuery<Long> countQuery = from(band)
                .where(band.name.contains(bandName)
                        .and(band.isDelete.isFalse()))
                .select(band.count());

        return PageableExecutionUtils.getPage(bandList, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
     *
     * @param userNo 회원 번호
     * @return 그룹 상세 정보 dto 리스트
     */
    @Override
    public List<GetBandDetailInfoResponseDto> getJoinedBandInfoListByUserNo(Long userNo) {
        QBandUser bandUser = QBandUser.bandUser;
        QAlingUser alingUser = QAlingUser.alingUser;
        QBand band = QBand.band;

        return from(bandUser)
                .innerJoin(bandUser.band, band)
                .innerJoin(bandUser.alingUser, alingUser)
                .where(alingUser.userNo.eq(userNo)
                        .and(alingUser.isDelete.isFalse())
                        .and(bandUser.isDelete.isFalse())
                        .and(bandUser.isBlock.isFalse())
                        .and(band.isDelete.isFalse()))
                .select(Projections.constructor(GetBandDetailInfoResponseDto.class,
                        band.bandNo,
                        band.name,
                        band.fileNo,
                        band.info,
                        band.isEnter,
                        band.isViewContent,
                        band.isUpload,
                        bandUser.bandUserNo,
                        bandUser.bandUserRole.bandUserRoleNo))
                .fetch();
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹 명
     * @return Optional 그룹 엔티티
     */
    @Override
    public Optional<Band> getByName(String bandName) {
        QBand band = QBand.band;

        return Optional.ofNullable(from(band)
                .where(band.isDelete.isFalse()
                        .and(band.name.eq(bandName)))
                .select(band)
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹 명
     * @return 그룹 존재 여부
     */
    @Override
    public boolean existsNonDeleteBandByName(String bandName) {
        QBand band = QBand.band;

        return (from(band)
                .where(band.isDelete.isFalse()
                        .and(band.name.eq(bandName)))
                .select(band)
                .fetchOne()) != null;
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹 명
     * @return 그룹 내 그룹 회원 수
     */
    @Override
    public long getCountBandUser(String bandName) {
        QBandUser bandUser = QBandUser.bandUser;
        QAlingUser alingUser = QAlingUser.alingUser;
        QBand band = QBand.band;

        return from(bandUser)
                .innerJoin(bandUser.band, band)
                .innerJoin(bandUser.alingUser, alingUser)
                .where(band.name.eq(bandName)
                        .and(alingUser.isDelete.isFalse())
                        .and(bandUser.isDelete.isFalse())
                        .and(bandUser.isBlock.isFalse())
                        .and(band.isDelete.isFalse()))
                .select(bandUser.count())
                .fetchOne();
    }
}
