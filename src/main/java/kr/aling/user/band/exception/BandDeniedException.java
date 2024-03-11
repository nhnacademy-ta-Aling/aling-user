package kr.aling.user.band.exception;

/**
 * Some description here.
 *
 * @author 정유진
 * @since 1.0
 **/
public class BandDeniedException extends RuntimeException {
    public static final String MESSAGE = "Band denied.";

    public BandDeniedException() {
        super(MESSAGE);
    }
}
