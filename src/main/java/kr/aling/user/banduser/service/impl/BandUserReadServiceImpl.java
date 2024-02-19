package kr.aling.user.banduser.service.impl;

import kr.aling.user.banduser.repository.BandUserReadRepository;
import kr.aling.user.banduser.service.BandUserReadService;
import kr.aling.user.common.dto.PageResponseDto;
import kr.aling.user.common.utils.PageUtils;
import kr.aling.user.user.dto.response.GetUserSimpleInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Some description here.
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
     * @param bandNo   그룹 번호
     * @param pageable pageable
     * @return 그룹 회원 정보 dto 페이지
     */
    @Override
    public PageResponseDto<GetUserSimpleInfoResponseDto> getBandUserList(Long bandNo, Pageable pageable) {
        return PageUtils.convert(bandUserReadRepository.getBandUserListByBandNo(bandNo, pageable));
    }
}
