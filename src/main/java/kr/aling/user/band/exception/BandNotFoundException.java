package kr.aling.user.band.exception;

/**
 * 그룹이 없을 경우 발생 하는 Exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Band not found.";

    public BandNotFoundException() {
        super(MESSAGE);
    }
}
