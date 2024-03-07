package kr.aling.user.common.aspect;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import kr.aling.user.common.exception.UserInfoNotExistsException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 유저의 정보를 주입하는 Aspect.
 *
 * @author 이수정
 * @since 1.0
 */
@Slf4j
@Aspect
@Component
public class InjectUserInfoAspect {

    /**
     * 게이트웨이에서 전달된 사용자의 정보를 얻어 요청 인자로 주입해줍니다.
     *
     * @param pjp 요청 인자를 전달하기 위한 ProceedingJoinPoint 객체
     * @return 실행 응답
     */
    @Around("@annotation(kr.aling.user.common.annotation.InjectUserInfo)")
    public Object injectUserInfo(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String userNo = request.getHeader("X-User-No");
        if (Objects.isNull(userNo)) {
            throw new UserInfoNotExistsException();
        }

        String[] parameterNames = ((MethodSignature) pjp.getSignature()).getParameterNames();
        Object[] args = pjp.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals("userNo")) {
                args[i] = Long.parseLong(userNo);
                break;
            }
        }

        return pjp.proceed(args);
    }
}
