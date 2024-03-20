package kr.aling.user.userrole.service;

/**
 * 회원권한 CUD Service interface.
 *
 * @author 이수정
 * @since 1.0
 */
public interface UserRoleManageService {

    /**
     * 회원번호로 기본권한("ROLE_USER")을 설정합니다.
     *
     * @param userNo 회원번호
     * @author 이수정
     * @since 1.0
     */
    void registerDefaultUserRole(long userNo);
}
