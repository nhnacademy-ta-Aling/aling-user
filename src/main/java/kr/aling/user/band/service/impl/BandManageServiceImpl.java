package kr.aling.user.band.service.impl;

import kr.aling.user.band.dto.request.CreateBandPostTypeRequestDto;
import kr.aling.user.band.dto.request.CreateBandRequestDto;
import kr.aling.user.band.dto.request.ModifyBandPostTypeRequestDto;
import kr.aling.user.band.dto.request.ModifyBandRequestDto;
import kr.aling.user.band.dto.request.external.CreateBandPostTypeRequestExternalDto;
import kr.aling.user.band.dto.request.external.ModifyBandPostTypeRequestExternalDto;
import kr.aling.user.band.entity.Band;
import kr.aling.user.band.exception.BandAlreadyExistsException;
import kr.aling.user.band.exception.BandDeniedException;
import kr.aling.user.band.exception.BandLimitExceededException;
import kr.aling.user.band.exception.BandNotFoundException;
import kr.aling.user.band.repository.BandManageRepository;
import kr.aling.user.band.repository.BandReadRepository;
import kr.aling.user.band.service.BandManageService;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduser.exception.BandUserNotFoundException;
import kr.aling.user.banduser.repository.BandUserManageRepository;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduserrole.entity.BandUserRole;
import kr.aling.user.banduserrole.exception.BandUserRoleNotFoundException;
import kr.aling.user.banduserrole.repository.BandUserRoleReadRepository;
import kr.aling.user.common.enums.BandPostTypeEnum;
import kr.aling.user.common.enums.BandUserRoleEnum;
import kr.aling.user.common.feignclient.PostFeignClient;
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
    private final PostFeignClient postFeignClient;

    /**
     * {@inheritDoc}
     *
     * @throws BandUserRoleNotFoundException 그룹 회원 권한을 찾을 수 없을 때 발생 excpetion
     * @throws BandLimitExceededException    생성할 수 있는 그룹 개수를 초과 했을 때 발생 excpetion
     * @throws BandAlreadyExistsException    그룹이 이미 존재할 경우 발생 exception
     */
    @Override
    public void makeBand(Long userNo, CreateBandRequestDto createBandRequestDto) {

        BandUserRole bandUserRole = bandUserRoleReadRepository.findByRoleName(BandUserRoleEnum.CREATOR.getRoleName())
                .orElseThrow(BandUserRoleNotFoundException::new);

        if (bandUserReadRepository.countByUserNoAndBandUserRoleName(userNo, bandUserRole.getRoleName())
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

        postFeignClient.requestMakeBandPostType(
                new CreateBandPostTypeRequestExternalDto(band.getBandNo(), BandPostTypeEnum.DEFAULT.getTypeName()));

        bandUserManageRepository.save(BandUser.builder()
                .bandUserRole(bandUserRole)
                .band(band)
                .alingUser(alingUser)
                .build());
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName  수정할 그룹 이름
     * @param modifyDto 수정할 정보를 담은 dto
     * @throws BandAlreadyExistsException 그룹 명이 이미 존재할 경우 발생 exception
     * @throws BandNotFoundException      그룹을 찾을 수 없을 경우 발생 exception
     */
    @Override
    public void updateBandInfo(String bandName, ModifyBandRequestDto modifyDto) {
        if (!bandName.equals(modifyDto.getNewBandName())
                && bandReadRepository.existsBandByName(modifyDto.getNewBandName())) {
            throw new BandAlreadyExistsException();
        }

        Band band = bandReadRepository.getByName(bandName)
                .orElseThrow(BandNotFoundException::new);

        band.updateBand(modifyDto.getNewBandName(),
                modifyDto.getBandInfo(),
                modifyDto.getIsEnter(),
                modifyDto.getIsViewContent(),
                modifyDto.getFileNo());
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName 삭제할 그룹 이름
     * @throws BandNotFoundException 그룹을 찾을 수 없을 때 발생 exception
     */
    @Override
    public void removeBand(String bandName) {
        Band band = bandReadRepository.getByName(bandName)
                .orElseThrow(BandNotFoundException::new);

        if (bandReadRepository.getCountBandUser(bandName) > 1) {
            throw new BandDeniedException();
        }

        BandUser bandCreator =
                bandUserReadRepository.findByBand_NameAndBandUserRole_RoleName(bandName,
                        BandUserRoleEnum.CREATOR.getRoleName()).orElseThrow(BandUserNotFoundException::new);

        bandCreator.deleteBandUser();
        band.deleteBand();
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName   그룹 게시글 분류를 생성할 그룹 명
     * @param requestDto 그룹 게시글 분류 생성을 위한 정보를 담은 dto
     * @throws BandNotFoundException 그룹을 찾을 수 없을 경우 발생 exception
     */
    @Override
    public void makeBandPostType(String bandName, CreateBandPostTypeRequestDto requestDto) {
        Band band = bandReadRepository.getByName(bandName)
                .orElseThrow(BandNotFoundException::new);

        postFeignClient.requestMakeBandPostType(
                new CreateBandPostTypeRequestExternalDto(band.getBandNo(), requestDto.getName()));
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName   그룹 명
     * @param postTypeNo 수정할 그룹 게시글 분류 번호
     * @param requestDto 그룹 게시글 분류 수정 정보를 담은 dto
     * @throws BandNotFoundException 그룹이 존재 하지 않을 경우 발생 exception
     */
    @Override
    public void modifyBandPostType(String bandName, Long postTypeNo, ModifyBandPostTypeRequestDto requestDto) {
        Band band = bandReadRepository.getByName(bandName)
                .orElseThrow(BandNotFoundException::new);

        postFeignClient.requestUpdateBandPostType(postTypeNo,
                new ModifyBandPostTypeRequestExternalDto(band.getBandNo(), requestDto.getBandPostTypeName()));
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName   그룹 명
     * @param postTypeNo 삭제할 그룹 게시글 분류 번호
     * @throws BandNotFoundException 그룹이 존재 하지 않을 경우 발생 exception
     */
    @Override
    public void deleteBandPostType(String bandName, Long postTypeNo) {
        if (!bandReadRepository.existsNonDeleteBandByName(bandName)) {
            throw new BandNotFoundException();
        }

        postFeignClient.requestDeleteBandPostType(postTypeNo);
    }
}
