package kr.aling.user.user.service;

import kr.aling.user.user.entity.AlingUser;

/**
 * 회원 읽기 Service interface.
 *
 * @author 이수정
 * @since 1.0
 */
public interface UserReadService {

    /**
     * 존재하는 이메일인지 확인합니다.
     *
     * @param email 확인할 이메일
     * @return 이메일 존재 여부
     * @author 이수정
     * @since 1.0
     */
    boolean isExistsEmail(String email);

    /**
     * 존재하는 회원인지 확인합니다.
     *
     * @param userNo 확인할 회원의 번호
     * @return 회원 존재 여부
     * @author 이수정
     * @since 1.0
     */
    boolean isExistsUserNo(Long userNo);

    /**
     * 회원 번호로 AlingUser를 조회합니다.
     *
     * @param userNo 조회할 회원의 번호
     * @return 조회된 회원 Entity
     * @author 이수정
     * @since 1.0
     */
    AlingUser getAlingUserByUserNo(Long userNo);
}
