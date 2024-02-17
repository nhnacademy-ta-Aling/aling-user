package kr.aling.user.band.service.impl;

import java.util.List;
import java.util.Optional;
import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.exception.BandNotFoundException;
import kr.aling.user.band.repository.BandReadRepository;
import kr.aling.user.band.service.BandReadService;
import kr.aling.user.band.dto.response.GetBandInfoResponseDto;
import lombok.RequiredArgsConstructor;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public ExistsBandNameResponseDto existBandName(String bandName) {
        return new ExistsBandNameResponseDto(bandReadRepository.existsBandByName(bandName));
    }

    /**
     * {@inheritDoc}
     *
     * @throws BandNotFoundException 그룹을 찾을 수 없을 때 Exception
     */
    @Override
    public GetBandDetailInfoResponseDto getBandDetailInfo(String bandName, Long userNo) {
        return bandReadRepository.getBandDetailInfoByBandName(bandName, userNo)
                .orElseThrow(BandNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetBandInfoResponseDto> getSearchBandInfoList(String bandName) {
        return bandReadRepository.getSearchBandInfoListByBandName(bandName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetBandInfoResponseDto> getJoinedBandInfoList(Long userNo) {
        return bandReadRepository.getJoinedBandInfoListByUserNo(userNo);
    }
}
