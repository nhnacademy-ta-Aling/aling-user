package kr.aling.user.banduser.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import kr.aling.user.band.entity.QBand;
import kr.aling.user.banduser.dto.response.BandPostUerQueryDto;
import kr.aling.user.banduser.dto.response.GetBandUserAndUserInfoResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserAuthResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserInfoResponseDto;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduser.entity.QBandUser;
import kr.aling.user.banduser.repository.BandUserReadRepositoryCustom;
import kr.aling.user.banduserrole.entity.QBandUserRole;
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
     * @param bandName 그룹 명
     * @param pageable pageable
     * @return 그룹 회원 정보 dto 페이지
     */
    @Override
    public Page<GetBandUserAndUserInfoResponseDto> getBandUserListByBandName(String bandName, Pageable pageable) {
        QBand band = QBand.band;
        QBandUser bandUser = QBandUser.bandUser;
        QAlingUser alingUser = QAlingUser.alingUser;
        QBandUserRole bandUserRole = QBandUserRole.bandUserRole;

        List<GetBandUserAndUserInfoResponseDto> bandUserList = from(bandUser)
                .innerJoin(bandUser.alingUser, alingUser)
                .innerJoin(bandUser.band, band)
                .innerJoin(bandUser.bandUserRole, bandUserRole)
                .where(bandUser.band.name.eq(bandName)
                        .and(bandUser.band.isDelete.isFalse())
                        .and(bandUser.isDelete.isFalse())
                        .and(bandUser.isBlock.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(Projections.constructor(GetBandUserAndUserInfoResponseDto.class,
                        Projections.constructor(GetBandUserInfoResponseDto.class,
                                bandUser.bandUserNo,
                                bandUser.bandUserRole.bandUserRoleNo),
                        Projections.constructor(GetUserSimpleInfoResponseDto.class,
                                alingUser.userNo,
                                alingUser.fileNo,
                                alingUser.name)
                ))
                .orderBy(bandUser.bandUserRole.bandUserRoleNo.asc(),
                        bandUser.enterAt.asc())
                .fetch();

        JPQLQuery<Long> countQuery = from(bandUser)
                .innerJoin(bandUser.alingUser, alingUser)
                .innerJoin(bandUser.band, band)
                .innerJoin(bandUser.bandUserRole, bandUserRole)
                .where(bandUser.band.name.eq(bandName)
                        .and(bandUser.band.isDelete.isFalse())
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
                                .and(band.isDelete.isFalse())
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
     * @param bandNo 그룹 번호
     * @param userNo 회원 번호
     * @return 그룹 회원 권한 정보 dto
     */
    @Override
    public Optional<GetBandUserAuthResponseDto> getBandUserInfoByBandNoAndUserNo(Long bandNo, Long userNo) {
        QBandUser bandUser = QBandUser.bandUser;
        QBandUserRole bandUserRole = QBandUserRole.bandUserRole;
        QBand band = QBand.band;
        QAlingUser alingUser = QAlingUser.alingUser;

        return Optional.ofNullable(
                from(bandUser)
                        .innerJoin(bandUser.bandUserRole, bandUserRole)
                        .innerJoin(bandUser.band, band)
                        .innerJoin(bandUser.alingUser, alingUser)
                        .where(alingUser.isDelete.isFalse()
                                .and(alingUser.isBlock.isFalse())
                                .and(band.isDelete.isFalse())
                                .and(bandUser.isDelete.isFalse())
                                .and(bandUser.isBlock.isFalse())
                                .and(bandUser.alingUser.userNo.eq(userNo))
                                .and(bandUser.band.bandNo.eq(bandNo)))
                        .select(Projections.constructor(GetBandUserAuthResponseDto.class,
                                bandUserRole.roleName
                        ))
                        .fetchOne());
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹 명
     * @param userNo   회원 번호
     * @return 그룹 회원 entity
     */
    @Override
    public Optional<BandUser> findBandUserByBandNameAndUserNo(String bandName, Long userNo) {
        QBandUser bandUser = QBandUser.bandUser;
        QAlingUser alingUser = QAlingUser.alingUser;
        QBand band = QBand.band;

        return Optional.ofNullable(
                from(bandUser)
                        .innerJoin(bandUser.band, band)
                        .innerJoin(bandUser.alingUser, alingUser)
                        .where(bandUser.alingUser.userNo.eq(userNo)
                                .and(band.name.eq(bandName))
                                .and(alingUser.isDelete.isFalse())
                                .and(band.isDelete.isFalse())
                                .and(bandUser.isBlock.isFalse())
                                .and(bandUser.isDelete.isFalse()))
                        .select(bandUser)
                        .fetchOne());
    }

    /**
     * 특정 회원이 특정 그룹 회원 권한을 몇 개 가지고 있는지 조회 하기 위한 메서드.
     *
     * @param userNo   회원 번호
     * @param roleName 그룹 회원 권한 이름
     * @return 회원의 권한 개수
     */
    @Override
    public long countByUserNoAndBandUserRoleName(Long userNo, String roleName) {
        QBandUser bandUser = QBandUser.bandUser;
        QBandUserRole bandUserRole = QBandUserRole.bandUserRole;
        QAlingUser alingUser = QAlingUser.alingUser;
        QBand band = QBand.band;

        return from(bandUser)
                .innerJoin(bandUser.bandUserRole, bandUserRole)
                .innerJoin(bandUser.band, band)
                .innerJoin(bandUser.alingUser, alingUser)
                .where(bandUser.alingUser.userNo.eq(userNo)
                        .and(bandUserRole.roleName.eq(roleName))
                        .and(alingUser.isDelete.isFalse())
                        .and(band.isDelete.isFalse())
                        .and(bandUser.isBlock.isFalse())
                        .and(bandUser.isDelete.isFalse()))
                .select(bandUser.count())
                .fetchOne();
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
