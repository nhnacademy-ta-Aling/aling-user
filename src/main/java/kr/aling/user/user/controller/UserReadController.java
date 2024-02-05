package kr.aling.user.user.controller;

import javax.validation.Valid;
import kr.aling.user.common.exception.CustomException;
import kr.aling.user.common.response.ApiResponse;
import kr.aling.user.user.dto.resquest.IsExistsUserRequestDto;
import kr.aling.user.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 조회 RestController.
 *
 * @author : 이수정
 * @since : 1.0
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserReadController {

    private final UserReadService userReadService;

    /**
     * 회원 존재여부 확인 요청을 처리합니다.
     *
     * @param requestDto    회원 존재여부 확인에 필요한 요청 파라미터를 담은 Dto
     * @param bindingResult 요청 파라미터 검증 오류를 보관하는 객체
     * @return 회원 존재 여부
     * @author : 이수정
     * @since : 1.0
     */
    @GetMapping
    public ApiResponse<Boolean> isExistsUser(
            @Valid @RequestBody IsExistsUserRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    "is exists user valid error - " + bindingResult.getAllErrors());
        }
        return new ApiResponse<>(
                true, HttpStatus.OK, userReadService.isExistsUserNo(requestDto.getUserNo()), "");
    }
}
