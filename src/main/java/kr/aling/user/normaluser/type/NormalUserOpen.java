package kr.aling.user.normaluser.type;

import lombok.Getter;

/**
 * 일반 회원의 공개 상태입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
public enum NormalUserOpen {
    ALL("ALL"),
    COMPANY("COMPANY"),
    PRIVATE("PRIVATE");

    private final String value;

    NormalUserOpen(String value) {
        this.value = value;
    }
}
