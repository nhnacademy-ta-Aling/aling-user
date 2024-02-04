package kr.aling.user.banduser.dummy;

import static java.time.LocalDateTime.now;

import kr.aling.user.band.entity.Band;
import kr.aling.user.banduser.entity.BandUser;
import kr.aling.user.banduserrole.entity.BandUserRole;
import kr.aling.user.user.entity.User;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class BandUserDummy {
    public static BandUser dummy(BandUserRole bandUserRole, Band band, User user) {
        return BandUser.builder()
                .bandUserRole(bandUserRole)
                .band(band)
                .user(user)
                .enterAt(now())
                .build();

    }
}
