package kr.aling.user.banduser.service.impl;

import kr.aling.user.band.entity.Band;
import kr.aling.user.band.exception.BandDeniedException;
import kr.aling.user.band.exception.BandNotFoundException;
import kr.aling.user.band.repository.BandReadRepository;
import kr.aling.user.banduser.dto.request.ModifyRoleOfBandUserRequestDto;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduser.exception.BandUserAlreadyExistsException;
import kr.aling.user.banduser.exception.BandUserNotFoundException;
import kr.aling.user.banduser.exception.BandUserRoleDeniedException;
import kr.aling.user.banduser.repository.BandUserManageRepository;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduser.service.BandUserManageService;
import kr.aling.user.banduserrole.entity.BandUserRole;
import kr.aling.user.banduserrole.exception.BandUserRoleNotFoundException;
import kr.aling.user.banduserrole.repository.BandUserRoleReadRepository;
import kr.aling.user.common.enums.BandUserRoleEnum;
import kr.aling.user.user.entity.AlingUser;
import kr.aling.user.user.exception.UserNotFoundException;
import kr.aling.user.user.repository.UserReadRepository;
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
    private final BandUserManageRepository bandUserManageRepository;
    private final BandUserRoleReadRepository bandUserRoleReadRepository;
    private final BandReadRepository bandReadRepository;
    private final UserReadRepository userReadRepository;

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹 명
     * @param userNo   회원 번호
     * @throws BandNotFoundException          그룹을 찾을 수 없을 때 발생 하는 exception
     * @throws UserNotFoundException          회원을 찾을 수 없을 때 발생 하는 exception
     * @throws BandUserRoleNotFoundException  그룹 회원 권한을 찾을 수 없을 때 발생 하는 exception
     * @throws BandUserAlreadyExistsException 그룹 회원이 이미 존재할 경우 발생 하는 exception
     * @throws BandDeniedException            추방된 회원일 경우 발생 하는 exception
     */
    @Override
    public void makeBandUser(String bandName, Long userNo) {
        Band band = bandReadRepository.getByName(bandName).orElseThrow(BandNotFoundException::new);
        AlingUser user = userReadRepository.getByUserNo(userNo).orElseThrow(UserNotFoundException::new);
        BandUserRole userRole = bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.USER.getRoleName())
                .orElseThrow(BandUserRoleNotFoundException::new);

        if (bandUserReadRepository.findBandUserByBandNameAndUserNo(bandName, userNo)
                .isPresent()) {
            throw new BandUserAlreadyExistsException();
        }

        if (bandUserReadRepository.getIsBlockBandUser(bandName, userNo)) {
            throw new BandDeniedException();
        }

        bandUserManageRepository.save(
                BandUser.builder()
                        .alingUser(user)
                        .band(band)
                        .bandUserRole(userRole)
                        .build());
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹 명
     * @param userNo   회원 번호
     * @throws BandUserNotFoundException   그룹 회원이 존재 하지 않을 때 발생 exception
     * @throws BandUserRoleDeniedException 그룹의 creator 인 경우 발생 exception
     */
    @Override
    public void removeBandUser(String bandName, Long userNo) {
        BandUser bandUser = bandUserReadRepository.findBandUserByBandNameAndUserNo(bandName, userNo)
                .orElseThrow(BandUserNotFoundException::new);

        if (bandUser.getBandUserRole().getRoleName().equals(BandUserRoleEnum.CREATOR.getRoleName())) {
            throw new BandUserRoleDeniedException();
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
