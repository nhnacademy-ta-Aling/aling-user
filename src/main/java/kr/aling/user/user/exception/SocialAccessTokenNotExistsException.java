package kr.aling.user.user.exception;

/**
 * 소셜 AccessToken 요청 후 잘못된 code로 인한 예외나 다른 이유로, AccessToken을 받지 못할 때 발생하는 예외.
 *
 * @author 이수정
 * @since 1.0
 */
public class SocialAccessTokenNotExistsException extends RuntimeException {

    private static final String MESSAGE = "잘못된 code로 인한 예외나 다른 이유로, AccessToken을 받지 못하였습니다.";

    public SocialAccessTokenNotExistsException() {
        super(MESSAGE);
    }
}
