package kr.aling.user.banduser.exception;

/**
 * creator 권한 임에도 그룹을 탈퇴 하려고 했을 때 발생 exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandUserRoleDeniedException extends RuntimeException {
    public static final String MESSAGE = "Creator can't leave band.";

    public BandUserRoleDeniedException() {
        super(MESSAGE);
    }
}
