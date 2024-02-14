package kr.aling.user.normaluser.dummy;

import java.time.LocalDate;
import kr.aling.user.normaluser.entity.NormalUser;
import kr.aling.user.user.entity.User;
import kr.aling.user.wantjobtype.entity.WantJobType;

/**
 * 일반회원 테스트용 Dummy class.
 *
 * @author : 이수정
 * @since : 1.0
 */
public class NormalUserDummy {

    public static NormalUser dummy(User user, WantJobType wantJobType) {
        return NormalUser.builder()
                .userNo(user.getUserNo())
                .wantJobType(wantJobType)
                .phoneNo("01000000000")
                .birth(LocalDate.now())
                .build();
    }
}
