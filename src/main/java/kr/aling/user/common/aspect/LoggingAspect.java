package kr.aling.user.common.aspect;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * API 요청과 응답을 로깅을 남기기 위한 AOP.
 *
 * @author 정유진
 * @since 1.0
 **/
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * controller package 에 있는 모든 메서드 실행 전 동작 하는 AOP. <br>
     * 요청 Method, uri 로깅.
     */
    @Before("execution(* kr.aling.user.*.controller..*(..))")
    public void doRequestLogging() {
        HttpServletRequest request = getHttpServletRequest();

        String uri = request.getRequestURI();
        log.info("API REQUEST : [{}] {}", request.getMethod(), uri);
    }

    /**
     * controller package 에 있는 모든 메서드 실행 후 정상 응답시 동작 하는 AOP. <br>
     * uri, status code 로깅.
     *
     * @param result ResponseEntity<?> 값
     */
    @AfterReturning(pointcut = "execution(* kr.aling.user.*.controller..*(..))", returning = "result")
    public void doResponseLogging(ResponseEntity<?> result) {
        HttpServletRequest request = getHttpServletRequest();

        String uri = request.getRequestURI();
        log.info("API RESPONSE : {} [{}]", uri, result.getStatusCode());
    }


    /**
     * API 응답 중 Exception 발생 후 ControllerAdvice 에서 처리 시 동작 하는 AOP. <br>
     * uri, status code 로깅.
     *
     * @param result ResponseEntity<?> 값
     */
    @AfterReturning(pointcut =
            "execution(* kr.aling.user.common.advice.ControllerAdvice.*(..))", returning = "result")
    public void doErrorResponseLogging(ResponseEntity<?> result) {
        HttpServletRequest request = getHttpServletRequest();

        String uri = request.getRequestURI();
        log.warn("API RESPONSE : {} [{}]", uri, result.getStatusCode());
    }

    /**
     * HttpServletRequest 꺼내 오는 메서드.
     *
     * @return HttpServletRequest
     */
    private static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}