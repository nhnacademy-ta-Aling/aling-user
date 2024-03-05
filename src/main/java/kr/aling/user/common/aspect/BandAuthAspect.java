package kr.aling.user.common.aspect;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import kr.aling.user.common.enums.BandUserRoleEnum;
import kr.aling.user.common.utils.ConstantUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 그룹 회원 권한을 확인 하기 위한 aspect.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Aspect
@Component
public class BandAuthAspect {

    /**
     * 그룹 회원 권한이 Creator 이상 인지 확인 하는 메서드.<br>
     *
     * @param pjp pjp
     * @return Object
     * @throws Throwable Throwable
     */
    @Around(value = "@annotation(kr.aling.user.common.annotation.BandCreatorAuth)")
    public Object checkBandCreatorAuth(ProceedingJoinPoint pjp) throws Throwable {
        String roleValue = getHeaderValue(ConstantUtil.X_BAND_USER_ROLE);

        if (Objects.nonNull(roleValue) && hasCreatorAuth(roleValue)) {
            return pjp.proceed(pjp.getArgs());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * 그룹 회원 권한이 Admin 이상 인지 확인 하는 메서드.<br> 그룹 회원 권한이 Creator, Admin 인 경우 권한이 확인 됩니다.
     *
     * @param pjp pjp
     * @return Object
     * @throws Throwable Throwable
     */
    @Around(value = "@annotation(kr.aling.user.common.annotation.BandAdminAuth)")
    public Object checkBandAdminAuth(ProceedingJoinPoint pjp) throws Throwable {
        String roleValue = getHeaderValue(ConstantUtil.X_BAND_USER_ROLE);

        if (Objects.nonNull(roleValue) && (hasCreatorAuth(roleValue) || hasAdminAuth(roleValue))) {
            return pjp.proceed(pjp.getArgs());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * 그룹 회원 권한이 User 이상 인지 확인 하는 메서드.<br> 그룹 회원 권한이 Creator, Admin, User 인 경우 권한이 확인 됩니다.
     *
     * @param pjp pjp
     * @return Object
     * @throws Throwable Throwable
     */
    @Around(value = "@annotation(kr.aling.user.common.annotation.BandUserAuth)")
    public Object checkBandUserAuth(ProceedingJoinPoint pjp) throws Throwable {
        String roleValue = getHeaderValue(ConstantUtil.X_BAND_USER_ROLE);

        if (Objects.nonNull(roleValue)
                && (hasUserAuth(roleValue) || hasAdminAuth(roleValue) || hasCreatorAuth(roleValue))) {
            return pjp.proceed(pjp.getArgs());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * 헤더 이름을 통해 값을 얻어 오는 메서드 입니다.
     *
     * @param headerName 헤더 이름
     * @return 헤더 값
     */
    private String getHeaderValue(String headerName) {
        ServletRequestAttributes servletRequestAttributes
                = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        HttpServletRequest request = servletRequestAttributes.getRequest();

        return request.getHeader(headerName);
    }

    /**
     * 회원 권한이 Creator 인지 확인 하는 메서드 입니다.
     *
     * @param value 권한 값
     * @return creator 유무
     */
    private boolean hasCreatorAuth(String value) {
        return value.equals(BandUserRoleEnum.CREATOR.getRoleName());
    }

    /**
     * 회원 권한이 Admin 인지 확인 하는 메서드 입니다.
     *
     * @param value 권한 값
     * @return admin 유무
     */
    private boolean hasAdminAuth(String value) {
        return value.equals(BandUserRoleEnum.ADMIN.getRoleName());
    }

    /**
     * 회원 권한이 User 인지 확인 하는 메서드 입니다.
     *
     * @param value 권한 값
     * @return user 유무
     */
    private boolean hasUserAuth(String value) {
        return value.equals(BandUserRoleEnum.USER.getRoleName());
    }
}
