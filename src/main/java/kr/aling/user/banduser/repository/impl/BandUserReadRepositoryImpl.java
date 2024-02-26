package kr.aling.user.banduser.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import kr.aling.user.band.entity.QBand;
import kr.aling.user.banduser.dto.response.BandPostUerQueryDto;
import kr.aling.user.banduser.dto.response.GetBandUserInfoResponseDto;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduser.entity.QBandUser;
import kr.aling.user.banduser.repository.BandUserReadRepositoryCustom;
import kr.aling.user.user.dto.response.GetUserSimpleInfoResponseDto;
import kr.aling.user.user.entity.QAlingUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

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

    /**
     * {@inheritDoc}
     *
     * @param bandNo   그룹 번호
     * @param pageable pageable
     * @return 그룹 회원 정보 dto 페이지
     */
    @Override
    public Page<GetUserSimpleInfoResponseDto> getBandUserListByBandNo(Long bandNo, Pageable pageable) {
        QBandUser bandUser = QBandUser.bandUser;
        QAlingUser alingUser = QAlingUser.alingUser;

        List<GetUserSimpleInfoResponseDto> bandUserList = from(bandUser)
                .innerJoin(bandUser.alingUser, alingUser)
                .where(bandUser.band.bandNo.eq(bandNo)
                        .and(bandUser.isDelete.isFalse())
                        .and(bandUser.isBlock.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(Projections.constructor(GetUserSimpleInfoResponseDto.class,
                        alingUser.userNo,
                        alingUser.fileNo,
                        alingUser.name))
                .fetch();

        JPQLQuery<Long> countQuery = from(bandUser)
                .innerJoin(bandUser.alingUser, alingUser)
                .where(bandUser.band.bandNo.eq(bandNo)
                        .and(bandUser.isDelete.isFalse())
                        .and(bandUser.isBlock.isFalse()))
                .select(bandUser.count());

        return PageableExecutionUtils.getPage(bandUserList, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹명
     * @param userNo   회원 번호
     * @return 그룹 회원 정보 dto
     */
    @Override
    public Optional<GetBandUserInfoResponseDto> getBandUserInfoByUserNoAndBandName(String bandName, Long userNo) {
        QBandUser bandUser = QBandUser.bandUser;
        QBand band = QBand.band;

        return Optional.ofNullable(
                from(bandUser)
                        .innerJoin(bandUser.band, band)
                        .where(bandUser.alingUser.userNo.eq(userNo)
                                .and(band.name.eq(bandName))
                                .and(bandUser.isBlock.isFalse())
                                .and(bandUser.isDelete.isFalse()))
                        .select(Projections.constructor(GetBandUserInfoResponseDto.class,
                                bandUser.bandUserNo,
                                bandUser.bandUserRole.bandUserRoleNo))
                        .fetchOne());
    }

    /**
     * {@inheritDoc}
     *
     * @param bandUserNo 그룹 회원 번호(작성자)
     * @return 회원(작성자) 정보 Dto
     */
    @Override
    public BandPostUerQueryDto getBandUserForPost(Long bandUserNo) {
        QBandUser bandUser = QBandUser.bandUser;
        QAlingUser alingUser = QAlingUser.alingUser;

        return from(bandUser)
                .innerJoin(bandUser.alingUser, alingUser)
                .select(Projections.constructor(BandPostUerQueryDto.class,
                        alingUser.userNo, alingUser.name, alingUser.fileNo))
                .where(bandUser.bandUserNo.eq(bandUserNo))
                .fetchOne();
    }
}
