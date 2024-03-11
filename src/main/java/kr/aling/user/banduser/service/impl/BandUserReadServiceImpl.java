package kr.aling.user.banduser.service.impl;

import java.util.Objects;
import kr.aling.user.banduser.dto.response.BandPostUerQueryDto;
import kr.aling.user.banduser.dto.response.GetBandUserAndUserInfoResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserAuthResponseDto;
import kr.aling.user.banduser.dto.response.GetPostWriterResponseDto;
import kr.aling.user.banduser.dto.response.external.GetFileInfoResponseDto;
import kr.aling.user.banduser.exception.BandUserNotFoundException;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduser.service.BandUserReadService;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.common.feignclient.FileFeignClient;
import kr.aling.user.common.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 그룹 회원 조회 service 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BandUserReadServiceImpl implements BandUserReadService {

    private final BandUserReadRepository bandUserReadRepository;
    private final FileFeignClient fileFeignClient;

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹 명
     * @param pageable pageable
     * @return 그룹 회원 정보 dto 페이지
     */
    @Override
    public PageResponseDto<GetBandUserAndUserInfoResponseDto> getBandUserList(String bandName, Pageable pageable) {
        return PageUtils.convert(bandUserReadRepository.getBandUserListByBandName(bandName, pageable));
    }

    /**
     * {@inheritDoc}
     *
     * @param bandNo 그룹 번호
     * @param userNo 회원 번호
     * @return 그룹 회원 권한 정보 dto
     */
    @Override
    public GetBandUserAuthResponseDto getBandUserInfo(Long bandNo, Long userNo) {
        return bandUserReadRepository.getBandUserInfoByBandNoAndUserNo(bandNo, userNo)
                .orElseThrow(BandUserNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     *
     * @param bandUserNo 그룹 회원 번호
     * @return 회원의 프로필 사진이 없다면 인자가 2개인 Dto, 프로필 사진이 있다면 인자가 3개인 Dto.
     */
    @Override
    public GetPostWriterResponseDto getPostWriterInfo(Long bandUserNo) {
        if (!bandUserReadRepository.existsById(bandUserNo)) {
            throw new BandUserNotFoundException();
        }

        BandPostUerQueryDto bandPostUerQueryDto = bandUserReadRepository.getBandUserForPost(bandUserNo);

        if (Objects.isNull(bandPostUerQueryDto.getFileNo())) {
            return new GetPostWriterResponseDto(
                    bandPostUerQueryDto.getUserNo(),
                    bandPostUerQueryDto.getUsername());
        } else {
            GetFileInfoResponseDto getFileInfoResponseDto =
                    fileFeignClient.requestFileInfo(bandPostUerQueryDto.getFileNo());

            return new GetPostWriterResponseDto(
                    bandPostUerQueryDto.getUserNo(),
                    bandPostUerQueryDto.getUsername(),
                    getFileInfoResponseDto.getPath());
        }
    }
}
