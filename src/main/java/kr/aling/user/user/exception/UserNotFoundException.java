package kr.aling.user.user.exception;

/**
 * 회원을 찾을 수 없을 때 발생 하는 Exception.
 *
 * @author 정유진
 * @since 1.0
 */
public class UserNotFoundException extends RuntimeException {

    public static final String MESSAGE = "User not found!";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
