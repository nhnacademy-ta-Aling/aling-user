package kr.aling.user.user.service;

import kr.aling.user.user.dto.request.LoginRequestDto;
import kr.aling.user.user.dto.response.LoginResponseDto;

/**
 * 로그인 Service interface.
 *
 * @author 여운석, 이수정
 * @since 1.0
 */
public interface LoginService {

    /**
     * 일반 로그인합니다.
     *
     * @param loginRequestDto 이메일과 비밀번호
     * @return 로그인한 회원의 정보
     * @author 여운석
     * @since 1.0
     */
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    /**
     * Oauth GitHub 로그인합니다.
     *
     * @param code GitHub AccessToken을 요청하기 위한 code
     * @return 로그인한 회원의 정보
     * @author 이수정
     * @since 1.0
     */
    LoginResponseDto github(String code);
}
