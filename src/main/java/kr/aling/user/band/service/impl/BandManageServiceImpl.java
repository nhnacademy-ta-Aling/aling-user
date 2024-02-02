package kr.aling.user.band.service.impl;

import kr.aling.user.band.dto.request.CreateBandRequestDto;
import kr.aling.user.band.entity.Band;
import kr.aling.user.band.exception.BandAlreadyExistsException;
import kr.aling.user.band.repository.BandManageRepository;
import kr.aling.user.band.repository.BandReadRepository;
import kr.aling.user.band.service.BandManageService;
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
    private final BandReadRepository bandReadRepository;
    private final BandManageRepository bandManageRepository;

    @Override
    public void makeBand(Long userNo, CreateBandRequestDto createBandRequestDto) {
        // user 로직 추가 예정

        if (bandReadRepository.existsBandByName(createBandRequestDto.getBandName())) {
            throw new BandAlreadyExistsException();
        }

        Band band = Band.builder()
                .name(createBandRequestDto.getBandName())
                .isEnter(createBandRequestDto.getIsEnter())
                .isViewContent(createBandRequestDto.getIsViewContent())
                .fileNo(createBandRequestDto.getFileNo())
                .build();

        bandManageRepository.save(band);
    }
}
