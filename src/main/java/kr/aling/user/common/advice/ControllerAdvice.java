package kr.aling.user.common.advice;

import javax.validation.ConstraintViolationException;
import kr.aling.user.band.exception.BandAccessDeniedException;
import kr.aling.user.band.exception.BandAlreadyExistsException;
import kr.aling.user.band.exception.BandDeniedException;
import kr.aling.user.band.exception.BandLimitExceededException;
import kr.aling.user.band.exception.BandNotFoundException;
import kr.aling.user.banduser.exception.BandUserAlreadyExistsException;
import kr.aling.user.banduser.exception.BandUserNotFoundException;
import kr.aling.user.banduser.exception.BandUserRoleDeniedException;
import kr.aling.user.banduserrole.exception.BandUserRoleNotFoundException;
import kr.aling.user.common.exception.CustomException;
import kr.aling.user.common.exception.UserInfoNotExistsException;
import kr.aling.user.mail.exception.MailAuthNumberInvalidException;
import kr.aling.user.post.exception.PostNotFoundException;
import kr.aling.user.user.exception.GithubAccessTokenNotExistsException;
import kr.aling.user.user.exception.SocialEmailNotFoundException;
import kr.aling.user.user.exception.UserEmailAlreadyUsedException;
import kr.aling.user.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 핸들링 class.
 *
 * @author 이수정
 * @since 1.0
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    private static final String DEFAULT_HANDLE_MESSAGE = "[{}] {}";

    /**
     * Http Status 400에 해당하는 예외를 공통 처리합니다.
     *
     * @param e 400에 해당하는 예외
     * @return 400 status response
     * @author 이수정
     * @since 1.0
     */
    @ExceptionHandler({ConstraintViolationException.class, MailAuthNumberInvalidException.class,
            BandLimitExceededException.class, BandDeniedException.class, BandUserRoleDeniedException.class})
    public ResponseEntity<String> handleBadRequestException(Exception e) {
        log.error(DEFAULT_HANDLE_MESSAGE, HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Http Status 401에 해당하는 예외를 공통 처리합니다.
     *
     * @param e 401에 해당하는 예외
     * @return 401 status response
     * @author 이수정
     * @since 1.0
     */
    @ExceptionHandler({UserInfoNotExistsException.class, GithubAccessTokenNotExistsException.class})
    public ResponseEntity<String> handleUnAuthorizedException(Exception e) {
        log.error(DEFAULT_HANDLE_MESSAGE, HttpStatus.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * Http Status 403에 해당하는 예외를 공통 처리 합니다.
     *
     * @param e 403에 해당하는 예외
     * @return 403 status response
     */
    @ExceptionHandler(BandAccessDeniedException.class)
    public ResponseEntity<String> handleForbiddenException(Exception e) {
        log.error(DEFAULT_HANDLE_MESSAGE, HttpStatus.FORBIDDEN, e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    /**
     * Http Status 404에 해당하는 예외를 공통 처리 합니다.
     *
     * @param e 404에 해당하는 예외
     * @return 404 status response
     */
    @ExceptionHandler({BandUserNotFoundException.class, BandUserRoleNotFoundException.class,
            BandNotFoundException.class, PostNotFoundException.class,
            UserNotFoundException.class, SocialEmailNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(Exception e) {
        log.error(DEFAULT_HANDLE_MESSAGE, HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Http Status 409에 해당하는 예외를 공통 처리합니다.
     *
     * @param e 409에 해당하는 예외
     * @return 409 status response
     * @author 이수정
     * @since 1.0
     */
    @ExceptionHandler({UserEmailAlreadyUsedException.class, BandAlreadyExistsException.class,
            BandUserAlreadyExistsException.class})
    public ResponseEntity<String> handleConflictException(Exception e) {
        log.error(DEFAULT_HANDLE_MESSAGE, HttpStatus.CONFLICT, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    /**
     * 핸들 지정되지 않은 예외를 공통 처리합니다.
     *
     * @param e CustomException
     * @return CustomException의 Http status response
     * @author 이수정
     * @since 1.0
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException e) {
        log.error(DEFAULT_HANDLE_MESSAGE, e.getHttpStatus(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
