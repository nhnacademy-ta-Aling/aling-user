package kr.aling.user.post.exception;

/**
 * 게시물이 존재하지 않는 경우 발생하는 Exception.
 *
 * @author 이수정
 * @since 1.0
 */
public class PostNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Post not found - ";

    public PostNotFoundException(Long postNo) {
        super(MESSAGE + postNo);
    }
}
