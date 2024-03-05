package kr.aling.user.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HttpStatus와 메세지를 담은 공용 Exception.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public CustomException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
