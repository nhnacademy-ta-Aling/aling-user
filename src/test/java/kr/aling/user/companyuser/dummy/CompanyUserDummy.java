package kr.aling.user.companyuser.dummy;

import kr.aling.user.companyuser.entity.CompanyUser;
import kr.aling.user.user.entity.AlingUser;

/**
 * 법인회원 테스트용 더미 클래스.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class CompanyUserDummy {
    public static CompanyUser dummy(AlingUser alingUser) {
        return CompanyUser.builder()
                .alingUser(alingUser)
                .companySize("CORPORATE")
                .registrationNo("1234567890")
                .sector("IT 업종")
                .build();
    }

}
