package kr.aling.user.common.advice;

import kr.aling.user.band.exception.BandLimitExceededException;
import kr.aling.user.common.exception.CustomException;
import kr.aling.user.common.response.ApiResponse;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserEmailAlreadyUsedException.class)
    public ApiResponse<String> handleConflictException(Exception e) {
        log.error("[{}] {}", HttpStatus.CONFLICT, e.getMessage());
        return new ApiResponse<>(false, e.getMessage(), null);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomException(CustomException e) {
        log.error("[{}] {}", e.getHttpStatus(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(new ApiResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(BandLimitExceededException.class)
    public ResponseEntity<String> wrongApproach(RuntimeException e) {
        log.warn("[{}]{}", HttpStatus.BAD_REQUEST, e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
