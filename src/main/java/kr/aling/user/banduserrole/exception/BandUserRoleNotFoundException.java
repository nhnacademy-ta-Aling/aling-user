package kr.aling.user.banduserrole.exception;

/**
 * 그룹 회원 권한을 찾지 못했을 때 발생 하는 Exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandUserRoleNotFoundException extends RuntimeException {

    public static final String MESSAGE = "BandUserRole is not found!";

    public BandUserRoleNotFoundException() {
        super(MESSAGE);
    }
}
