package kr.aling.user.wantjobtype.service.impl;

import kr.aling.user.wantjobtype.dto.response.ReadWantJobTypeResponseDto;
import kr.aling.user.wantjobtype.exception.WantJobTypeNotFoundException;
import kr.aling.user.wantjobtype.repository.WantJobTypeReadRepository;
import kr.aling.user.wantjobtype.service.WantJobTypeReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 구직희망타입 조회 Service 구현체.
 *
 * @author : 이수정
 * @since : 1.0
 */
@RequiredArgsConstructor
@Transactional
@Service
public class WantJobTypeReadServiceImpl implements WantJobTypeReadService {

    private final WantJobTypeReadRepository wantJobTypeReadRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public ReadWantJobTypeResponseDto findByWantJobTypeNo(Integer wantJobTypeNo) {
        return new ReadWantJobTypeResponseDto(
                wantJobTypeReadRepository.findById(wantJobTypeNo)
                        .orElseThrow(() -> new WantJobTypeNotFoundException(wantJobTypeNo)));
    }
}
