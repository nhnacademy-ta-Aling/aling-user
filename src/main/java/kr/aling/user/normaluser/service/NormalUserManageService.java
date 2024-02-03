package kr.aling.user.normaluser.service;

import kr.aling.user.normaluser.dto.request.CreateNormalUserRequestDto;
import kr.aling.user.normaluser.dto.response.CreateNormalUserResponseDto;

/**
 * 일반회원 CUD Service interface.
 *
 * @author : 이수정
 * @since : 1.0
 */
public interface NormalUserManageService {

    /**
     * 일반회원을 등록합니다.
     *
     * @param requestDto 일반회원 등록에 필요한 요청 파라미터를 담은 Dto
     * @return 일반회원 등록 후 일부 데이터를 담은 Dto 반환
     * @author : 이수정
     * @since : 1.0
     */
    CreateNormalUserResponseDto registerNormalUser(CreateNormalUserRequestDto requestDto);
}
