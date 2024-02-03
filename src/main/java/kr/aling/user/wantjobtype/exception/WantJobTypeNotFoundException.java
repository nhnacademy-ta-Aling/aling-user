package kr.aling.user.wantjobtype.exception;

/**
 * 일치하는 구직희망타입을 찾을 수 없는 경우, 예외를 던집니다.
 *
 * @author : 이수정
 * @since : 1.0
 */
public class WantJobTypeNotFoundException extends RuntimeException {

    private static final String MSG = "WantJobTypeNo %s is Not Found.";

    public WantJobTypeNotFoundException(Integer wantJobTypeNo) {
        super(String.format(MSG, wantJobTypeNo));
    }
}
