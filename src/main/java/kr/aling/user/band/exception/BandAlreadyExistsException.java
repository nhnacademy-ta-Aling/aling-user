package kr.aling.user.band.exception;

/**
 * 그룹이 이미 존재할 경우 발생하는 Exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandAlreadyExistsException extends RuntimeException {

    public static final String MESSAGE = "Band already exists.";

    public BandAlreadyExistsException() {
        super(MESSAGE);
    }
}
