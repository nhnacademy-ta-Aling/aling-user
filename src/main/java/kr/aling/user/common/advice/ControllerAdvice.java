package kr.aling.user.common.advice;

import javax.validation.ConstraintViolationException;
import kr.aling.user.common.exception.CustomException;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 핸들링 class.
 *
 * @author : 이수정
 * @since : 1.0
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * Http Status 400에 해당하는 예외를 공통 처리합니다.
     *
     * @param e 400에 해당하는 예외
     * @return 400 status response
     * @author : 이수정
     * @since : 1.0
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleBadRequestException(Exception e) {
        log.error("[{}] {}", HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Http Status 409에 해당하는 예외를 공통 처리합니다.
     *
     * @param e 409에 해당하는 예외
     * @return 409 status response
     * @author : 이수정
     * @since : 1.0
     */
    @ExceptionHandler(UserEmailAlreadyUsedException.class)
    public ResponseEntity<String> handleConflictException(Exception e) {
        log.error("[{}] {}", HttpStatus.CONFLICT, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    /**
     * 핸들 지정되지 않은 예외를 공통 처리합니다.
     *
     * @param e CustomException
     * @return CustomException의 Http status response
     * @author : 이수정
     * @since : 1.0
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException e) {
        log.error("[{}] {}", e.getHttpStatus(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
