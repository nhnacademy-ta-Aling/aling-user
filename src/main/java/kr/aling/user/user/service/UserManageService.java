package kr.aling.user.user.service;

import kr.aling.user.user.dto.response.CreateUserResponseDto;
import kr.aling.user.user.dto.resquest.CreateUserRequestDto;

/**
 * 회원 CUD Service interface.
 *
 * @author : 이수정
 * @since : 1.0
 */
public interface UserManageService {

    /**
     * 회원을 등록합니다.
     *
     * @param requestDto 회원 등록에 필요한 요청 파라미터를 담은 Dto
     * @return 회원 등록 후 회원 No를 담은 Dto 반환
     * @author : 이수정
     * @since : 1.0
     */
    CreateUserResponseDto registerUser(CreateUserRequestDto requestDto);
}
