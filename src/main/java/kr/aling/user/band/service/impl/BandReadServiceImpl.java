package kr.aling.user.band.service.impl;

import kr.aling.user.band.dto.response.ExistsBandNameResponseDto;
import kr.aling.user.band.repository.BandReadRepository;
import kr.aling.user.band.service.BandReadService;
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
}
