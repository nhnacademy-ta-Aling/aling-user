package kr.aling.user.user.exception;

/**
 * 소셜에 회원 정보 요청 후 회원의 소셜에 Email이 설정되지 않아 Email을 받지 못하거나, 받은 Email에 해당하는 회원을 찾을 수 없는 경우 발생하는 예외.
 *
 * @author 이수정
 * @since 1.0
 */
public class SocialEmailNotFoundException extends RuntimeException {

    public SocialEmailNotFoundException() {
        super("회원의 Email을 찾을 수 없습니다.");
    }
}
