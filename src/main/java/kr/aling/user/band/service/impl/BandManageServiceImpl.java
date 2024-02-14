package kr.aling.user.band.service.impl;

import java.time.LocalDateTime;
import kr.aling.user.band.dto.request.CreateBandRequestDto;
import kr.aling.user.band.entity.Band;
import kr.aling.user.band.exception.BandAlreadyExistsException;
import kr.aling.user.band.exception.BandLimitExceededException;
import kr.aling.user.band.repository.BandManageRepository;
import kr.aling.user.band.repository.BandReadRepository;
import kr.aling.user.band.service.BandManageService;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduser.repository.BandUserManageRepository;
import kr.aling.user.banduser.repository.BandUserReadRepository;
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
 * 그룹(Band)을 관리하는 Service 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class BandManageServiceImpl implements BandManageService {
    private static final int USER_OWN_BAND_LIMIT = 3;

    private final BandReadRepository bandReadRepository;
    private final BandManageRepository bandManageRepository;
    private final UserReadRepository userReadRepository;
    private final BandUserRoleReadRepository bandUserRoleReadRepository;
    private final BandUserReadRepository bandUserReadRepository;
    private final BandUserManageRepository bandUserManageRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeBand(Long userNo, CreateBandRequestDto createBandRequestDto) {

        BandUserRole bandUserRole = bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.CREATOR.getRoleName())
                .orElseThrow(BandUserRoleNotFoundException::new);

        if (bandUserReadRepository.countByAlingUser_UserNoAndBandUserRole_RoleName(userNo, bandUserRole.getRoleName())
                >= USER_OWN_BAND_LIMIT) {
            throw new BandLimitExceededException();
        }

        if (bandReadRepository.existsBandByName(createBandRequestDto.getBandName())) {
            throw new BandAlreadyExistsException();
        }

        AlingUser alingUser = userReadRepository.findById(userNo).orElseThrow(UserNotFoundException::new);

        Band band = bandManageRepository.save(Band.builder()
                .name(createBandRequestDto.getBandName())
                .isEnter(createBandRequestDto.getIsEnter())
                .isViewContent(createBandRequestDto.getIsViewContent())
                .info(createBandRequestDto.getBandInfo())
                .fileNo(createBandRequestDto.getFileNo())
                .build());

        bandUserManageRepository.save(BandUser.builder()
                .bandUserRole(bandUserRole)
                .band(band)
                .alingUser(alingUser)
                .enterAt(LocalDateTime.now())
                .build());
    }
}
