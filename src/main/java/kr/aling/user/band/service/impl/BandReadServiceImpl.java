package kr.aling.user.band.service.impl;

import java.util.List;
import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
import kr.aling.user.band.dto.response.GetBandInfoWithBandUserResponseDto;
import kr.aling.user.band.exception.BandNotFoundException;
import kr.aling.user.band.repository.BandReadRepository;
import kr.aling.user.band.service.BandReadService;
import kr.aling.user.banduser.dto.response.GetBandUserInfoResponseDto;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.common.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 그룹(Band)을 조회 하기 위한 Service 구현체.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BandReadServiceImpl implements BandReadService {
    private final BandReadRepository bandReadRepository;
    private final BandUserReadRepository bandUserReadRepository;

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹명
     * @return 그룹명 존재 여부 dto
     */
    @Override
    public ExistsBandNameResponseDto existBandName(String bandName) {
        return new ExistsBandNameResponseDto(bandReadRepository.existsBandByName(bandName));
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹명
     * @param userNo   유저 번호
     * @return 그룹 및 그룹 유저 정보 dto
     * @throws BandNotFoundException 그룹을 찾을 수 없을 때 Exception
     */
    @Override
    public GetBandInfoWithBandUserResponseDto getBandDetailInfo(String bandName, Long userNo) {
        GetBandInfoResponseDto bandInfo = bandReadRepository.getBandDetailInfoByBandName(bandName)
                .orElseThrow(BandNotFoundException::new);

        GetBandUserInfoResponseDto bandUserInfo =
                bandUserReadRepository.getBandUserInfoByUserNoAndBandName(bandName, userNo)
                        .orElse(null);

        return new GetBandInfoWithBandUserResponseDto(bandInfo, bandUserInfo);
    }

    /**
     * {@inheritDoc}
     *
     * @param bandName 그룹명
     * @param pageable pageable
     * @return 그룹 정보 dto 페이지
     */
    @Override
    public PageResponseDto<GetBandInfoResponseDto> getSearchBandInfoList(String bandName, Pageable pageable) {
        return PageUtils.convert(bandReadRepository.getSearchBandInfoListByBandName(bandName, pageable));
    }

    /**
     * {@inheritDoc}
     *
     * @param userNo 회원 번호
     * @return 그룹 상세 정보 dto 리스트
     */
    @Override
    public List<GetBandDetailInfoResponseDto> getJoinedBandInfoList(Long userNo) {
        return bandReadRepository.getJoinedBandInfoListByUserNo(userNo);
    }
}
