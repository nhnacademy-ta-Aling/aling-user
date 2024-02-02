package kr.aling.user.wantjobtype.dummy;

import kr.aling.user.wantjobtype.entity.WantJobType;

/**
 * 구직상태타입 테스트용 Dummy class.
 *
 * @author : 이수정
 * @since : 1.0
 */
public class WantJobTypeDummy {
    public static WantJobType dummy() {
        return new WantJobType(1, "ALLOWANCE");
    }
}
