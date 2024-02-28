package kr.aling.user.companyuser.controller;

import javax.validation.Valid;
import kr.aling.user.companyuser.dto.request.CreateCompanyUserRequestDto;
import kr.aling.user.companyuser.dto.response.CreateCompanyUserResponseDto;
import kr.aling.user.companyuser.service.CompanyUserManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CompanyUserManageController.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyUserManageController {

    private final CompanyUserManageService companyUserManageService;

    /**
     * 법인 회원 가입 컨트롤러.
     *
     * @param requestDto 회원가입시 필요한 정보
     * @return 가입한 법인명
     */
    @PostMapping
    public ResponseEntity<CreateCompanyUserResponseDto>
    registerCompanyUser(@RequestBody @Valid CreateCompanyUserRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyUserManageService.registerCompanyUser(requestDto));
    }
}
