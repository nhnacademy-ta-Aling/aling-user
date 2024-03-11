package kr.aling.user.common.exception;

/**
 * 사용자의 정보를 헤더를 통해 가져오지 못하는 경우 발생하는 Exception.
 *
 * @author 이수정
 * @since 1.0
 */
public class UserInfoNotExistsException extends RuntimeException {

    private static final String MESSAGE = "사용자의 정보가 존재하지 않는 요청입니다.";

    public UserInfoNotExistsException() {
        super(MESSAGE);
    }
}
