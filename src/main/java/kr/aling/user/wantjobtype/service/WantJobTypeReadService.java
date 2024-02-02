package kr.aling.user.wantjobtype.service;

import kr.aling.user.wantjobtype.dto.response.ReadWantJobTypeResponseDto;

/**
 * 구직희망타입 조회 Service interface.
 *
 * @author : 이수정
 * @since : 1.0
 */
public interface WantJobTypeReadService {

    /**
     * 구직희망타입 Id로 조회하여 구직희망타입을 담은 Dto를 반환합니다.
     *
     * @param wantJobTypeNo 구직희망타입 Id
     * @return 구직 구직희망타입을 담은 Dto
     * @author : 이수정
     * @since : 1.0
     */
    ReadWantJobTypeResponseDto findByWantJobTypeNo(Integer wantJobTypeNo);
}
