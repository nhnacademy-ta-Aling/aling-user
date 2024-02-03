package kr.aling.user.user.service;

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
    boolean existsEmail(String email);
}
