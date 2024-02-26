package kr.aling.user.banduser.service.impl;

import java.util.Objects;
import kr.aling.user.banduser.dto.response.BandPostUerQueryDto;
import kr.aling.user.banduser.dto.response.GetPostWriterResponseDto;
import kr.aling.user.banduser.dto.response.external.GetFileInfoResponseDto;
import kr.aling.user.banduser.exception.BandUserNotFoundException;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduser.service.BandUserReadService;
import kr.aling.user.common.adaptor.AlingFileAdaptor;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.common.utils.PageUtils;
import kr.aling.user.user.dto.response.GetUserSimpleInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 그룹 회원 Read Service 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BandUserReadServiceImpl implements BandUserReadService {

    private final BandUserReadRepository bandUserReadRepository;
    private final AlingFileAdaptor alingFileAdaptor;

    /**
     * {@inheritDoc}
     *
     * @param bandNo   그룹 번호
     * @param pageable pageable
     * @return 그룹 회원 정보 dto 페이지
     */
    @Override
    public PageResponseDto<GetUserSimpleInfoResponseDto> getBandUserList(Long bandNo, Pageable pageable) {
        return PageUtils.convert(bandUserReadRepository.getBandUserListByBandNo(bandNo, pageable));
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
                    alingFileAdaptor.requestFileInfo(bandPostUerQueryDto.getFileNo());

            return new GetPostWriterResponseDto(
                    bandPostUerQueryDto.getUserNo(),
                    bandPostUerQueryDto.getUsername(),
                    getFileInfoResponseDto.getPath());
        }
    }
}
