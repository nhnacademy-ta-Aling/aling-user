package kr.aling.user.banduser.exception;

/**
 * BandUser 없는 경우 Exception.
 *
 * @author 박경서
 * @since 1.0
 **/
public class BandUserNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Band User Not Found";

    public BandUserNotFoundException() {
        super(MESSAGE);
    }
}
