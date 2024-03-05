package kr.aling.user.band.exception;

/**
 * 그룹 접근이 거부될 때 발생 하는 exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandAccessDeniedException extends RuntimeException {

    public static final String MESSAGE = "Band access denied";

    public BandAccessDeniedException() {
        super(MESSAGE);
    }
}
