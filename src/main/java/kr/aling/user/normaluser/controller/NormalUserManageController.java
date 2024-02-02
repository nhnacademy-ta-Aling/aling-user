package kr.aling.user.normaluser.controller;

import javax.validation.Valid;
import kr.aling.user.normaluser.dto.request.CreateNormalUserRequestDto;
import kr.aling.user.normaluser.dto.response.CreateNormalUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 일반회원 CUD RestController.
 *
 * @author : 이수정
 * @since : 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/normals")
public class NormalUserManageController {

    /**
     * 일반회원가입 요청을 처리합니다.
     *
     * @param request       일반회원가입에 필요한 요청 파라미터를 담은 Dto
     * @param bindingResult 요청 파라미터 검증 오류를 보관하는 객체
     * @return 회원가입 후 일부 데이터 반환
     * @author : 이수정
     * @since : 1.0
     */
    @PostMapping
    public ResponseEntity<CreateNormalUserResponseDto> signUpNormalUser(
            @Valid @RequestBody CreateNormalUserRequestDto request, BindingResult bindingResult) {
        return null;
    }
}
