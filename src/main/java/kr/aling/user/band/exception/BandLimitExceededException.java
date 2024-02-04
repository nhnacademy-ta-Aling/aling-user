package kr.aling.user.band.exception;

/**
 * 그룹 소유 개수 초과 시 발생 하는 exception.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandLimitExceededException extends RuntimeException {

    public static final String MESSAGE = "Can't make band because band limit has been exceeded!";

    public BandLimitExceededException() {
        super(MESSAGE);
    }
}
