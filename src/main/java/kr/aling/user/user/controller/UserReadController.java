package kr.aling.user.user.controller;

import kr.aling.user.common.response.ApiResponse;
import kr.aling.user.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * @param userNo        회원 존재여부 확인할 회원 번호
     * @return 회원 존재 여부
     * @author : 이수정
     * @since : 1.0
     */
    @GetMapping("/check/{userNo}")
    public ApiResponse<Boolean> isExistsUser(@PathVariable Long userNo) {
        return new ApiResponse<>(true, "", userReadService.isExistsUserNo(userNo));
    }
}
