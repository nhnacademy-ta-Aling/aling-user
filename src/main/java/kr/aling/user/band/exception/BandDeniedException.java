package kr.aling.user.band.exception;

/**
 * 그룹에 대한 행위를 할 수 없을 경우 발생 exception.
 *
 * @author 정유진
 * @since 1.0
 **/
public class BandDeniedException extends RuntimeException {
    public static final String MESSAGE = "band denied";

    public BandDeniedException() {
        super(MESSAGE);
    }
}
