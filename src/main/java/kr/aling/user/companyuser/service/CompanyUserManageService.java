package kr.aling.user.companyuser.service;

import kr.aling.user.companyuser.dto.request.CompanyUserRegisterRequestDto;
import kr.aling.user.companyuser.dto.response.CompanyUserRegisterResponseDto;

/**
 * CompanyUserManageService class.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface CompanyUserManageService {

    /**
     * 법인 회원가입 서비스 메서드.
     *
     * @param requestDto 회원가입시 필요한 정보
     * @return 가입한 법인 명
     */
    CompanyUserRegisterResponseDto registerCompanyUser(CompanyUserRegisterRequestDto requestDto);
}
