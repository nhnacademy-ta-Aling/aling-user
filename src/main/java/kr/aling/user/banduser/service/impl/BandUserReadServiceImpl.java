package kr.aling.user.banduser.service.impl;

import kr.aling.user.banduser.dto.response.GetBandUserAndUserInfoResponseDto;
import kr.aling.user.banduser.dto.response.GetBandUserAuthResponseDto;
import kr.aling.user.banduser.exception.BandUserNotFoundException;
import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduser.service.BandUserReadService;
import kr.aling.user.common.dto.PageResponseDto;
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

    /**
     * {@inheritDoc}
     *
     * @param bandName   그룹 명
     * @param pageable pageable
     * @return 그룹 회원 정보 dto 페이지
     */
    @Override
    public PageResponseDto<GetBandUserAndUserInfoResponseDto> getBandUserList(String bandName, Pageable pageable) {
        return PageUtils.convert(bandUserReadRepository.getBandUserListByBandName(bandName, pageable));
    }

    @Override
    public GetBandUserAuthResponseDto getBandUserInfo(Long bandNo, Long userNo) {
        return bandUserReadRepository.getBandUserInfoByBandNoAndUserNo(bandNo, userNo)
                .orElseThrow(BandUserNotFoundException::new);
    }
}
