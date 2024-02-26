package kr.aling.user.user.controller;

import java.util.List;
import javax.validation.Valid;
import kr.aling.user.band.dto.response.GetBandDetailInfoResponseDto;
import kr.aling.user.band.service.BandReadService;
import kr.aling.user.common.utils.ConstantUtil;
import kr.aling.user.user.dto.response.IsExistsUserResponseDto;
import kr.aling.user.user.dto.response.LoginResponseDto;
import kr.aling.user.user.dto.resquest.LoginRequestDto;
import kr.aling.user.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    private final BandReadService bandReadService;

    /**
     * 회원 존재여부 확인 요청을 처리합니다.
     *
     * @param userNo 회원 존재여부 확인할 회원 번호
     * @return 회원 존재 여부
     * @author : 이수정
     * @since : 1.0
     */
    @GetMapping("/check/{userNo}")
    public ResponseEntity<IsExistsUserResponseDto> isExistsUser(@PathVariable Long userNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new IsExistsUserResponseDto(userReadService.isExistsUserNo(userNo)));
    }

    /**
     * 가입된 그룹 목록을 조회 하기 위한 메서드.
     *
     * @param userNo 회원 번호
     * @return 200 OK. 해당 회원이 가입한 그룹 목록
     */
    @GetMapping
    @RequestMapping("/my-bands")
    public ResponseEntity<List<GetBandDetailInfoResponseDto>> getJoinedBandInfoList(
            @RequestHeader(ConstantUtil.X_TEMP_USER_NO) Long userNo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bandReadService.getJoinedBandInfoList(userNo));
    }

    /**
     * Id와 비밀번호를 검증하여 로그인합니다.
     *
     * @param loginRequestDto id, 비밀번호
     * @return 로그인 정보
     */
    @GetMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userReadService.login(loginRequestDto));
    }
}
