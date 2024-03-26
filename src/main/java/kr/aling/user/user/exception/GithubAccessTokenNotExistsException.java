package kr.aling.user.user.exception;

/**
 * Github AccessToken 요청 후 잘못된 code로 인한 예외나 다른 이유로, AccessToken을 받지 못할 때 발생하는 예외.
 *
 * @author 이수정
 * @since 1.0
 */
public class GithubAccessTokenNotExistsException extends RuntimeException {

    public GithubAccessTokenNotExistsException() {
        super("잘못된 code로 인한 예외나 다른 이유로, AccessToken을 받지 못하였습니다.");
    }
}
