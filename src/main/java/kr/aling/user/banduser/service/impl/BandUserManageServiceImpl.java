package kr.aling.user.banduser.service.impl;

import kr.aling.user.banduser.dto.request.ModifyRoleOfBandUserRequestDto;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduser.exception.BandUserNotFoundException;
import kr.aling.user.banduser.exception.CreatorDeniedException;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduser.service.BandUserManageService;
import kr.aling.user.banduserrole.entity.BandUserRole;
import kr.aling.user.banduserrole.exception.BandUserRoleNotFoundException;
import kr.aling.user.banduserrole.repository.BandUserRoleReadRepository;
import kr.aling.user.common.enums.BandUserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 그룹 회원 관리 service 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class BandUserManageServiceImpl implements BandUserManageService {

    private final BandUserReadRepository bandUserReadRepository;
    private final BandUserRoleReadRepository bandUserRoleReadRepository;

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹 명
     * @param userNo   회원 번호
     * @throws BandUserNotFoundException 그룹 회원이 존재 하지 않을 때 발생 exception
     * @throws CreatorDeniedException    creator 권한이 없어 접근 거부일 때 발생 exception
     */
    @Override
    public void removeBandUser(String bandName, Long userNo) {
        BandUser bandUser = bandUserReadRepository.findBandUserByBandNameAndUserNo(bandName, userNo)
                .orElseThrow(BandUserNotFoundException::new);

        BandUserRole creatorRole = bandUserRoleReadRepository.findByRoleName(
                BandUserRoleEnum.CREATOR.getRoleName()).orElseThrow(BandUserRoleNotFoundException::new);

        if (bandUser.getBandUserRole().getBandUserRoleNo().equals(creatorRole.getBandUserRoleNo())) {
            throw new CreatorDeniedException();
        }

        bandUser.deleteBandUser();
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName                       그룹 명
     * @param userNo                         회원 번호
     * @param modifyRoleOfBandUserRequestDto 그룹 회원 권한 수정 정보를 담은 dto
     * @throws BandUserNotFoundException     그룹 회원이 존재 하지 않을 때 발생 exception
     * @throws BandUserRoleNotFoundException 그룹 회원 권한이 존재 하지 않을 때 발생 exception
     */
    @Override
    public void modifyRoleOfBandUser(String bandName, Long userNo,
            ModifyRoleOfBandUserRequestDto modifyRoleOfBandUserRequestDto) {
        BandUser bandUser = bandUserReadRepository.findBandUserByBandNameAndUserNo(bandName, userNo)
                .orElseThrow(BandUserNotFoundException::new);

        BandUserRole bandUserRole =
                bandUserRoleReadRepository.findById(modifyRoleOfBandUserRequestDto.getBandUserRoleNo())
                        .orElseThrow(BandUserRoleNotFoundException::new);

        bandUser.updateBandUserRole(bandUserRole);
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName     그룹 명
     * @param targetUserNo creator 권한을 위임 받을 회원 번호
     * @param userNo       creator 권한 회원 번호
     * @throws BandUserNotFoundException     그룹 회원이 존재 하지 않을 때 발생 exception
     * @throws BandUserRoleNotFoundException 그룹 회원 권한이 존재 하지 않을 때 발생 exception
     */
    @Override
    public void modifyCreatorRoleOfBandUser(String bandName, Long targetUserNo, Long userNo) {
        BandUser targetBandUser = bandUserReadRepository.findBandUserByBandNameAndUserNo(bandName, targetUserNo)
                .orElseThrow(BandUserNotFoundException::new);

        BandUserRole creatorRole = bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.CREATOR.getRoleName())
                .orElseThrow(BandUserRoleNotFoundException::new);

        BandUser bandUser = bandUserReadRepository.findBandUserByBandNameAndUserNo(bandName, userNo)
                .orElseThrow(BandUserNotFoundException::new);

        BandUserRole adminRole = bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.ADMIN.getRoleName())
                .orElseThrow(BandUserRoleNotFoundException::new);

        targetBandUser.updateBandUserRole(creatorRole);
        bandUser.updateBandUserRole(adminRole);
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹 명
     * @param userNo   회원 번호
     * @throws BandUserNotFoundException 그룹 회원이 존재 하지 않을 때 발생 exception
     */
    @Override
    public void modifyBlockOfBandUser(String bandName, Long userNo) {
        BandUser bandUser = bandUserReadRepository.findBandUserByBandNameAndUserNo(bandName, userNo)
                .orElseThrow(BandUserNotFoundException::new);

        bandUser.updateBandUserBlock();
        bandUser.updateBandUserDelete();
    }
}
