package kr.aling.user.user.service;

import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.dto.resquest.LoginRequestDto;

/**
 * 회원 읽기 전용 서비스.
 *
 * @author : 여운석
 * @author : 이수정
 * @since : 1.0
 **/
public interface UserReadService {

    /**
     * 존재하는 이메일인지 확인합니다.
     *
     * @param email 확인할 이메일
     * @return 이메일 존재 여부
     * @author : 이수정
     * @since : 1.0
     */
    boolean isExistsEmail(String email);

    /**
     * 존재하는 회원인지 확인합니다.
     *
     * @param userNo 확인할 회원의 번호
     * @return 회원 존재 여부
     * @author : 이수정
     * @since : 1.0
     */
    boolean isExistsUserNo(Long userNo);

    /**
     * 로그인합니다.
     *
     * @param loginRequestDto 이메일과 비밀번호
     * @return 로그인한 회원의 정보
     * @author : 여운석
     * @since : 1.0
     */
    LoginResponseDto login(LoginRequestDto loginRequestDto);

}
