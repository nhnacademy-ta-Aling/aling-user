package kr.aling.user.common.utils;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Header 관련 Util Class.
 *
 * @author 박경서
 * @since 1.0
 **/
public class HeaderUtil {

    /**
     * Util Class 기본 생성자.
     */
    private HeaderUtil() {
    }

    /**
     * 공통 사용 하는 HTTP Header 만드는 메서드.
     *
     * @return HttpHeader
     */
    public static HttpHeaders makeHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        return headers;
    }
}