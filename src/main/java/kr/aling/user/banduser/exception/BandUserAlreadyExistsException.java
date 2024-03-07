package kr.aling.user.banduser.exception;

/**
 * Some description here.
 *
 * @author 정유진
 * @since 1.0
 **/
public class BandUserAlreadyExistsException extends RuntimeException {
    public static final String MESSAGE = "Band user already exists.";

    public BandUserAlreadyExistsException() {
        super(MESSAGE);
    }
}
